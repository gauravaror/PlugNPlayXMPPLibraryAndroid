
package net.java.otr4j.session;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import net.java.otr4j.OtrPolicy;
import net.java.otr4j.test.TestStrings;
import net.java.otr4j.test.dummyclient.DummyClient;
import net.java.otr4j.test.dummyclient.PriorityServer;
import net.java.otr4j.test.dummyclient.ProcessedTestMessage;
import net.java.otr4j.test.dummyclient.Server;

import org.junit.Test;

public class SessionTest {

    @Test
    public void testMultipleSessions() throws Exception {
        DummyClient bob1 = new DummyClient("Bob@Wonderland");
        bob1.setPolicy(new OtrPolicy(OtrPolicy.ALLOW_V2 | OtrPolicy.ALLOW_V3
                | OtrPolicy.ERROR_START_AKE));

        DummyClient bob2 = new DummyClient("Bob@Wonderland");
        bob2.setPolicy(new OtrPolicy(OtrPolicy.ALLOW_V2 | OtrPolicy.ALLOW_V3
                | OtrPolicy.ERROR_START_AKE));

        DummyClient bob3 = new DummyClient("Bob@Wonderland");
        bob3.setPolicy(new OtrPolicy(OtrPolicy.ALLOW_V2 | OtrPolicy.ALLOW_V3
                | OtrPolicy.ERROR_START_AKE));

        DummyClient alice = new DummyClient("Alice@Wonderland");
        alice.setPolicy(new OtrPolicy(OtrPolicy.ALLOW_V2 | OtrPolicy.ALLOW_V3
                | OtrPolicy.ERROR_START_AKE));

        Server server = new PriorityServer();
        alice.connect(server);
        bob1.connect(server);
        bob2.connect(server);
        bob3.connect(server);

        bob1.send(alice.getAccount(), TestStrings.otrQuery);

        alice.pollReceivedMessage(); // Query
        bob1.pollReceivedMessage(); // DH-Commit
        alice.pollReceivedMessage(); // DH-Key
        bob1.pollReceivedMessage(); // Reveal signature
        alice.pollReceivedMessage(); // Signature

        String msg;

        alice.send(
                bob1.getAccount(),
                msg = "Hello Bob, this new IM software you installed on my PC the other day says we are talking Off-the-Record, what's that supposed to mean?");

        assertThat("Message has been transferred unencrypted.", alice
                .getConnection().getSentMessage(), not(equalTo(msg)));

        assertEquals("Received message is different from the sent message.",
                msg, bob1.pollReceivedMessage().getContent());

        bob2.send(alice.getAccount(), msg = TestStrings.anotherOtrQuery);

        alice.pollReceivedMessage();
        bob2.pollReceivedMessage();
        alice.pollReceivedMessage();
        bob2.pollReceivedMessage();
        alice.pollReceivedMessage();

        bob2.send(alice.getAccount(), msg = "This should be encrypted !");
        assertThat("Message has been transferred unencrypted.", bob2
                .getConnection().getSentMessage(), not(equalTo(msg)));

        assertEquals("Received message is different from the sent message.",
                msg, alice.pollReceivedMessage().getContent());

        bob3.send(alice.getAccount(), msg = TestStrings.yetAnotherOtrQuery);
        alice.pollReceivedMessage();
        bob3.pollReceivedMessage();
        alice.pollReceivedMessage();
        bob3.pollReceivedMessage();
        alice.pollReceivedMessage();

        bob3.send(alice.getAccount(), msg = "This should be encrypted !");
        assertThat("Message has been transferred unencrypted.", bob3
                .getConnection().getSentMessage(), not(equalTo(msg)));

        assertEquals("Received message is different from the sent message.",
                msg, alice.pollReceivedMessage().getContent());

        bob1.send(alice.getAccount(),
                msg = "Hey Alice, it means that our communication is encrypted and authenticated.");
        assertThat("Message has been transferred unencrypted.", bob1
                .getConnection().getSentMessage(), not(equalTo(msg)));

        assertEquals("Received message is different from the sent message.",
                msg, alice.pollReceivedMessage().getContent());

        alice.send(bob1.getAccount(), msg = "Oh, is that all?");
        assertThat("Message has been transferred unencrypted.", alice
                .getConnection().getSentMessage(), not(equalTo(msg)));

        assertEquals("Received message is different from the sent message.",
                msg, bob1.pollReceivedMessage().getContent());

        bob1.send(
                alice.getAccount(),
                msg = "Actually no, our communication has the properties of perfect forward secrecy and deniable authentication.");
        assertThat("Message has been transferred unencrypted.", bob1
                .getConnection().getSentMessage(), not(equalTo(msg)));

        assertEquals("Received message is different from the sent message.",
                msg, alice.pollReceivedMessage().getContent());

        alice.send(bob1.getAccount(), msg = "Oh really?! pouvons-nous parler en français?");
        assertThat("Message has been transferred unencrypted.", alice
                .getConnection().getSentMessage(), not(equalTo(msg)));

        assertEquals("Received message is different from the sent message.",
                msg, bob1.pollReceivedMessage().getContent());

        bob1.exit();
        alice.exit();
    }

    @Test
    public void testQueryStart() throws Exception {
        DummyClient[] convo = DummyClient.getConversation();
        DummyClient alice = convo[0];
        DummyClient bob = convo[1];

        bob.send(alice.getAccount(), TestStrings.otrQuery);

        alice.pollReceivedMessage(); // Query
        bob.pollReceivedMessage(); // DH-Commit
        alice.pollReceivedMessage(); // DH-Key
        bob.pollReceivedMessage(); // Reveal signature
        alice.pollReceivedMessage(); // Signature

        assertEquals("The session is not encrypted.", SessionStatus.ENCRYPTED,
                bob.getSession().getSessionStatus());
        assertEquals("The session is not encrypted.", SessionStatus.ENCRYPTED,
                alice.getSession().getSessionStatus());

        String msg;

        alice.send(
                bob.getAccount(),
                msg = "Hello Bob, this new IM software you installed on my PC the other day says we are talking Off-the-Record, what's that supposed to mean?");

        assertThat("Message has been transferred unencrypted.", alice
                .getConnection().getSentMessage(), not(equalTo(msg)));

        assertEquals("Received message is different from the sent message.",
                msg, bob.pollReceivedMessage().getContent());

        bob.send(alice.getAccount(),
                msg = "Hey Alice, it means that our communication is encrypted and authenticated.");
        assertThat("Message has been transferred unencrypted.", bob
                .getConnection().getSentMessage(), not(equalTo(msg)));

        assertEquals("Received message is different from the sent message.",
                msg, alice.pollReceivedMessage().getContent());

        alice.send(bob.getAccount(), msg = "Oh, is that all?");
        assertThat("Message has been transferred unencrypted.", alice
                .getConnection().getSentMessage(), not(equalTo(msg)));

        assertEquals("Received message is different from the sent message.",
                msg, bob.pollReceivedMessage().getContent());

        bob.send(
                alice.getAccount(),
                msg = "Actually no, our communication has the properties of perfect forward secrecy and deniable authentication.");
        assertThat("Message has been transferred unencrypted.", bob
                .getConnection().getSentMessage(), not(equalTo(msg)));

        assertEquals("Received message is different from the sent message.",
                msg, alice.pollReceivedMessage().getContent());

        alice.send(bob.getAccount(), msg = "Oh really?! pouvons-nous parler en français?");
        assertThat("Message has been transferred unencrypted.", alice
                .getConnection().getSentMessage(), not(equalTo(msg)));

        assertEquals("Received message is different from the sent message.",
                msg, bob.pollReceivedMessage().getContent());

        bob.exit();
        alice.exit();
    }

    @Test
    public void testForcedStart() throws Exception {
        DummyClient[] convo = DummyClient.getConversation();
        DummyClient alice = convo[0];
        DummyClient bob = convo[1];

        DummyClient.forceStartOtr(alice, bob);
        assertEquals("The session is not encrypted.", SessionStatus.ENCRYPTED,
                bob.getSession().getSessionStatus());
        assertEquals("The session is not encrypted.", SessionStatus.ENCRYPTED,
                alice.getSession().getSessionStatus());

        String msg;

        alice.send(
                bob.getAccount(),
                msg = "Hello Bob, this new IM software you installed on my PC the other day says we are talking Off-the-Record, what's that supposed to mean?");

        assertThat("Message has been transferred unencrypted.", alice
                .getConnection().getSentMessage(), not(equalTo(msg)));

        assertEquals("Received message is different from the sent message.",
                msg, bob.pollReceivedMessage().getContent());

        bob.send(alice.getAccount(),
                msg = "Hey Alice, it means that our communication is encrypted and authenticated.");
        assertThat("Message has been transferred unencrypted.", bob
                .getConnection().getSentMessage(), not(equalTo(msg)));

        assertEquals("Received message is different from the sent message.",
                msg, alice.pollReceivedMessage().getContent());

        alice.send(bob.getAccount(), msg = "Oh, is that all?");
        assertThat("Message has been transferred unencrypted.", alice
                .getConnection().getSentMessage(), not(equalTo(msg)));

        assertEquals("Received message is different from the sent message.",
                msg, bob.pollReceivedMessage().getContent());

        bob.send(
                alice.getAccount(),
                msg = "Actually no, our communication has the properties of perfect forward secrecy and deniable authentication.");
        assertThat("Message has been transferred unencrypted.", bob
                .getConnection().getSentMessage(), not(equalTo(msg)));

        assertEquals("Received message is different from the sent message.",
                msg, alice.pollReceivedMessage().getContent());

        alice.send(bob.getAccount(), msg = "Oh really?! pouvons-nous parler en français?");
        assertThat("Message has been transferred unencrypted.", alice
                .getConnection().getSentMessage(), not(equalTo(msg)));

        assertEquals("Received message is different from the sent message.",
                msg, bob.pollReceivedMessage().getContent());

        bob.exit();
        alice.exit();
    }

    @Test
    public void testPlaintext() throws Exception {
        DummyClient[] convo = DummyClient.getConversation();
        DummyClient alice = convo[0];
        DummyClient bob = convo[1];

        String msg;

        alice.send(bob.getAccount(),
                msg = "Hello Bob, this new IM software you installed on my PC the other day says we are talking Off-the-Record, what's that supposed to mean?");

        ProcessedTestMessage pMsg = bob.pollReceivedMessage();

        assertEquals("The session is not encrypted.", SessionStatus.PLAINTEXT,
                bob.getSession().getSessionStatus());
        assertEquals("The session is not encrypted.", SessionStatus.PLAINTEXT,
                alice.getSession().getSessionStatus());

        assertEquals("Message has been altered (but it shouldn't).", msg, alice
                .getConnection().getSentMessage());

        assertEquals("Received message is different from the sent message.",
                msg, pMsg.getContent());

        bob.send(alice.getAccount(),
                msg = "Hey Alice, it means that our communication is encrypted and authenticated.");

        assertEquals("Message has been altered (but it shouldn't).", msg, bob
                .getConnection().getSentMessage());

        pMsg = alice.pollReceivedMessage();
        assertEquals("Received message is different from the sent message.",
                msg, pMsg.getContent());

        alice.send(bob.getAccount(), msg = "Oh, is that all?");

        assertEquals("Message has been altered (but it shouldn't).", msg, alice
                .getConnection().getSentMessage());

        assertEquals("Received message is different from the sent message.",
                msg, bob.pollReceivedMessage().getContent());

        bob.send(alice.getAccount(),
                msg = "Actually no, our communication has the properties of perfect forward secrecy and deniable authentication.");

        assertEquals("Message has been altered (but it shouldn't).", msg, bob
                .getConnection().getSentMessage());

        pMsg = alice.pollReceivedMessage();
        assertEquals("Received message is different from the sent message.",
                msg, pMsg.getContent());

        alice.send(bob.getAccount(), msg = "Oh really?! pouvons-nous parler en français?");

        assertEquals("Message has been altered (but it shouldn't).", msg, alice
                .getConnection().getSentMessage());

        pMsg = bob.pollReceivedMessage();
        assertEquals("Received message is different from the sent message.",
                msg, pMsg.getContent());

        bob.exit();
        alice.exit();
    }
}
