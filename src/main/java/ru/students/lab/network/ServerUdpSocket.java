package ru.students.lab.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
/**
 * Класс для работы с сервером
 * @autor Хосе Ортис
 * @version 1.0
 */
public class ServerUdpSocket /*extends AbsUdpSocket*/ {

    protected static final Logger LOG = LogManager.getLogger(ServerUdpSocket.class);

    public static final int SOCKET_TIMEOUT = 3000;

    protected DatagramSocket socket;
    protected List<SocketAddress> clientList;

    public ServerUdpSocket(InetSocketAddress a) throws SocketException {
        socket = new DatagramSocket(a);
        socket.setSoTimeout(SOCKET_TIMEOUT);
        clientList = new ArrayList<>();
    }
    /**
     * Функция для создания и отправки датаграммы
     * @param content - отправляемые данные
     * @param client - адрес сокета клиента
     */
    //@Override
    public void sendDatagram(byte[] content, SocketAddress client) throws IOException {
        DatagramPacket packet = new DatagramPacket(content, content.length, client);
        socket.send(packet);

        LOG.info("Sent datagram from SERVER to " + client);
    }
    /**
     * Функция для получения данных и создания датаграммы
     * @param buffer - полученные данные
     * @return  адрес клиента, отправившего пакет
     */
    //@Override
    public SocketAddress receiveDatagram(ByteBuffer buffer) throws IOException {
        byte[] buf = new byte[buffer.remaining()];

        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        LOG.info("Received datagram in SERVER from " + packet.getSocketAddress());
        buffer.put(buf, 0, packet.getLength());
        return packet.getSocketAddress();
    }
    /**
     * Функция для сериализации данных, создания и отправки датаграммы
     * @param response - отправляемые данные
     * @param client - client that requested from data
     */
    public void sendResponse(Object response, SocketAddress client) {
        try(ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteArrayStream)) {

            objectStream.writeObject(response);
            LOG.info("send object " + response.toString());
            sendDatagram(byteArrayStream.toByteArray(), client);

        } catch (IOException e) {
            LOG.error("Problem sending the response", e);
        }
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void disconnect() {
        socket.disconnect();
    }
}
