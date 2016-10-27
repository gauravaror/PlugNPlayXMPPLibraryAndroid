/**
 * Copyright (c) 2013, Redsolution LTD. All rights reserved.
 *
 * This file is part of Xabber project; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License, Version 3.
 *
 * Xabber is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * along with this program. If not, see http://www.gnu.org/licenses/.
 */
package com.xabber.android.data.extension.otr;

import android.database.Cursor;

import com.xabber.android.R;
import com.xabber.android.data.Application;
import com.xabber.android.data.LogManager;
import com.xabber.android.data.NetworkException;
import com.xabber.android.data.OnCloseListener;
import com.xabber.android.data.OnLoadListener;
import com.xabber.android.data.SettingsManager;
import com.xabber.android.data.SettingsManager.SecurityOtrMode;
import com.xabber.android.data.account.AccountItem;
import com.xabber.android.data.account.AccountManager;
import com.xabber.android.data.account.OnAccountAddedListener;
import com.xabber.android.data.account.OnAccountRemovedListener;
import com.xabber.android.data.entity.NestedMap;
import com.xabber.android.data.entity.NestedNestedMaps;
import com.xabber.android.data.extension.archive.MessageArchiveManager;
import com.xabber.android.data.extension.ssn.SSNManager;
import com.xabber.android.data.message.AbstractChat;
import com.xabber.android.data.message.MessageManager;
import com.xabber.android.data.notification.EntityNotificationProvider;
import com.xabber.android.data.notification.NotificationManager;
import com.xabber.xmpp.archive.OtrMode;
import com.xabber.xmpp.archive.SaveMode;
import com.xabber.xmpp.archive.Session;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Manage off-the-record encryption.
 * <p/>
 * http://www.cypherpunks.ca/otr/
 *
 * @author alexander.ivanov
 */
public class OTRManager implements
        OnLoadListener, OnAccountAddedListener, OnAccountRemovedListener, OnCloseListener {

    private final static OTRManager instance;

    static {
        instance = new OTRManager();
        Application.getInstance().addManager(instance);
    }

    private final EntityNotificationProvider<SMRequest> smRequestProvider;
    private final EntityNotificationProvider<SMProgress> smProgressProvider;
    /**
     * Accepted fingerprints for user in account.
     */
    private final NestedNestedMaps<String, Boolean> fingerprints;
    /**
     * Fingerprint of encrypted or encrypted and verified session for user in account.
     */
    private final NestedMap<String> actives;
    /**
     * Finished entity's sessions for users in accounts.
     */
    private final NestedMap<Boolean> finished;
     /**
     * Service for keypair generation.
     */
    private final ExecutorService keyPairGenerator;

    private OTRManager() {
        smRequestProvider = new EntityNotificationProvider<>(R.drawable.ic_stat_help);
        smProgressProvider = new EntityNotificationProvider<>(R.drawable.ic_stat_play_circle_fill);
        smProgressProvider.setCanClearNotifications(false);
        fingerprints = new NestedNestedMaps<>();
        actives = new NestedMap<>();
        finished = new NestedMap<>();
        keyPairGenerator = Executors.newSingleThreadExecutor(new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable runnable) {
                        Thread thread = new Thread(runnable, "Key pair generator service");
                        thread.setPriority(Thread.MIN_PRIORITY);
                        thread.setDaemon(true);
                        return thread;
                    }
                });
    }

    public static OTRManager getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        final NestedNestedMaps<String, Boolean> fingerprints = new NestedNestedMaps<>();
        Cursor cursor = OTRTable.getInstance().list();
        try {
            if (cursor.moveToFirst()) {
                do {
                    String account = OTRTable.getAccount(cursor);
                    String user = OTRTable.getUser(cursor);
                    fingerprints.put(account, user,
                            OTRTable.getFingerprint(cursor),
                            OTRTable.isVerified(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
        Application.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onLoaded(fingerprints);
            }
        });
    }

    private void onLoaded(NestedNestedMaps<String, Boolean> fingerprints) {
        this.fingerprints.addAll(fingerprints);
        NotificationManager.getInstance().registerNotificationProvider(smRequestProvider);
        NotificationManager.getInstance().registerNotificationProvider(smProgressProvider);
    }

    public void startSession(String account, String user) throws NetworkException {
        LogManager.i(this, "Starting session for " + user);

        LogManager.i(this, "Started session for " + user);
    }

    public void refreshSession(String account, String user) throws NetworkException {
        LogManager.i(this, "Refreshing session for " + user);
        LogManager.i(this, "Refreshed session for " + user);
    }

    public void endSession(String account, String user) throws NetworkException {
        LogManager.i(this, "Ending session for " + user);
        AbstractChat abstractChat = MessageManager.getInstance().getChat(account, user);
        MessageArchiveManager.getInstance().setSaveMode(account, user, abstractChat.getThreadId(), SaveMode.body);
        SSNManager.getInstance().setSessionOtrMode(account, user, abstractChat.getThreadId(), OtrMode.concede);
        LogManager.i(this, "Ended session for " + user);
    }

    private Session getOrCreateSession(String account, String user) {
        return null;
    }



    /**
     * Transform outgoing message before sending.
     */
    public String transformSending(String account, String user, String content)  {
        LogManager.i(this, "transform outgoing message... " + user);
        return content;
    }

    /**
     * Transform incoming message after receiving.
     */
    public String transformReceiving(String account, String user, String content) {
        LogManager.i(this, "transform incoming message... " + content);
        return content;
    }

    public SecurityLevel getSecurityLevel(String account, String user) {
        if (actives.get(account, user) == null) {
            if (finished.get(account, user) == null) {
                return SecurityLevel.plain;
            } else {
                return SecurityLevel.finished;
            }
        } else {
            if (isVerified(account, user)) {
                return SecurityLevel.verified;
            } else {
                return SecurityLevel.encrypted;
            }
        }
    }

    public boolean isVerified(String account, String user) {
        String active = actives.get(account, user);
        if (active == null) {
            return false;
        }
        Boolean value = fingerprints.get(account, user, active);
        return value != null && value;
    }

    private void setVerifyWithoutNotification(String account, String user, String fingerprint, boolean value) {
        fingerprints.put(account, user, fingerprint, value);
        requestToWrite(account, user, fingerprint, value);
    }

    /**
     * Set whether fingerprint was verified. Add action to the chat history.
     */
    public void setVerify(String account, String user, String fingerprint, boolean value) {
        setVerifyWithoutNotification(account, user, fingerprint, value);
    }



    public String getRemoteFingerprint(String account, String user) {
        return actives.get(account, user);
    }

    public String getLocalFingerprint(String account) {

        return null;
    }


    /**
     * Respond using SM protocol.
     */
    public void respondSmp(String account, String user, String question, String secret) throws NetworkException {
        LogManager.i(this, "responding smp... " + user);
    }

    /**
     * Initiate request using SM protocol.
     */
    public void initSmp(String account, String user, String question, String secret) throws NetworkException {
        LogManager.i(this, "initializing smp... " + user);
    }

    /**
     * Abort SM negotiation.
     */
    public void abortSmp(String account, String user) throws NetworkException {
        LogManager.i(this, "aborting smp... " + user);

    }

    private void removeSMRequest(String account, String user) {
        smRequestProvider.remove(account, user);
    }

    private void addSMProgress(String account, String user) {
        smProgressProvider.add(new SMProgress(account, user), false);
    }

    private void removeSMProgress(String account, String user) {
        smProgressProvider.remove(account, user);
    }

    @Override
    public void onAccountAdded(final AccountItem accountItem) {
        if (accountItem.getKeyPair() != null) {
            return;
        }
        keyPairGenerator.execute(new Runnable() {
            @Override
            public void run() {
                LogManager.i(this, "KeyPair generation started for " + accountItem.getAccount());
                final KeyPair keyPair;
                try {
                    keyPair = KeyPairGenerator.getInstance("DSA").genKeyPair();
                } catch (final NoSuchAlgorithmException e) {
                    Application.getInstance().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            throw new RuntimeException(e);
                        }
                    });
                    return;
                }
                Application.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogManager.i(this, "KeyPair generation finished for " + accountItem.getAccount());
                        if (AccountManager.getInstance().getAccount(accountItem.getAccount()) != null) {
                            AccountManager.getInstance().setKeyPair(accountItem.getAccount(), keyPair);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onAccountRemoved(AccountItem accountItem) {
    }

    /**
     * Save chat specific otr settings.
     */
    private void requestToWrite(final String account, final String user,
                                final String fingerprint, final boolean verified) {
        Application.getInstance().runInBackground(new Runnable() {
            @Override
            public void run() {
                OTRTable.getInstance().write(account, user, fingerprint, verified);
            }
        });
    }

    private void endAllSessions() {

    }

    @Override
    public void onClose() {
        endAllSessions();
    }

    public void onSettingsChanged() {
        if (SettingsManager.securityOtrMode() == SecurityOtrMode.disabled) {
            endAllSessions();
        }
    }

    public void onContactUnAvailable(String account, String user) {
    }
}
