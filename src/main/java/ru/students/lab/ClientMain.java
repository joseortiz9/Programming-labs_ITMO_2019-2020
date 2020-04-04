package ru.students.lab;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.students.lab.client.IHandlerInput;
import ru.students.lab.client.UserInputHandler;
import ru.students.lab.udp.ClientUdpChannel;
import ru.students.lab.udp.ConsoleReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.*;
import java.util.Scanner;

public class ClientMain extends Thread {

    private static final Logger LOG = LogManager.getLogger(ClientMain.class);

        private static ClientUdpChannel channel = null;

    public static void main(String[] args) {
        InetSocketAddress address = null;
        try {
            final int port = Integer.parseInt(args[0]);
            if (args.length > 1) {
                final String host = args[1];
                address = new InetSocketAddress(host, port);
            }
            address = new InetSocketAddress(port);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Port isn't provided");
            LOG.error("Port isn't provided");
            System.exit(-1);
        } catch (IllegalArgumentException ex) {
            System.err.println("The provided port is out of the available range: " + args[0]);
            LOG.error("The provided port is out of the available range: " + args[0], ex);
            System.exit(-1);
        }

        try {
            final ClientUdpChannel oldChannel = channel;
            if (oldChannel != null) {
                channel.disconnect();
            }
            channel = new ClientUdpChannel();
            channel.tryToConnect(address);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Unable to connect to the server, check logs for detailed information");
            LOG.error("Unable to connect to the server", ex);
            System.exit(-1);
        }

        LOG.info("Successfully connected to the server");

        try {
            IHandlerInput userInputHandler = new UserInputHandler(true);
            ConsoleReader sender = new ConsoleReader(channel, userInputHandler);
            sender.setName("ConsoleReaderTread");
            sender.start();
        } finally {
            //channel.disconnect();
        }
    }
}