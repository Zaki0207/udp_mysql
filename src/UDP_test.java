import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDP_test {
    private static final int PORT = 8066;

    // int转byte[]
    private static byte[] toBytes(int number){
        // 小端：低位到高位
        byte[] bytes = new byte[4];
        bytes[0] = (byte)(number & 0xFF);
        bytes[1] = (byte)((number >> 8) & 0xFF);
        bytes[2] = (byte)((number >> 16) & 0xFF);
        bytes[3] = (byte)((number >> 24) & 0xFF);
        return bytes;
    }

    // byte[]转int
    private static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        // 大端：高位到低位，中转为小端
        byte[] temp = new byte[4];
        for (int i = 0; i < 4; i++){
            temp[i] = bytes[3-i];
        }
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (temp[i] & 0x000000FF) << shift;//往高位游
        }
        return value;
    }

    // byte[]转char[8]
    private static String byteArrayToString(byte[] bytes) throws UnsupportedEncodingException {
        byte[] temps = new byte[8];
        System.arraycopy(bytes, 0, temps, 0,8);
        String str = new String(temps, "UTF-8");
        return str;
    }

    // byte[]转double
    public static double bytes2Double(byte[] arr) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) (arr[i] & 0xff)) << (8 * i);
        }
        return Double.longBitsToDouble(value);
    }

    public Recv_data UDP_recv() {
        Recv_data temp_data = new Recv_data();
        try{
            DatagramSocket socket = new DatagramSocket(PORT);
            try{
                byte[] buf = new byte[1024];
                DatagramPacket recv_msg = new DatagramPacket(buf, buf.length);
                socket.receive(recv_msg);

                // 核对消息来源
                String ip = recv_msg.getAddress().getHostAddress();
                int port = recv_msg.getPort();

                byte[] datas = recv_msg.getData();
                int len=recv_msg.getLength();

                String data_msg = byteArrayToString(datas);
                temp_data.setMsg(data_msg);
                byte[] temp_data_1 = new byte[4];
                System.arraycopy(datas, 8, temp_data_1, 0,4);
                int data_1 = byteArrayToInt(temp_data_1);
                temp_data.setData_1(data_1);
                byte[] temp_data_2 = new byte[4];
                System.arraycopy(datas, 12, temp_data_2, 0,4);
                int data_2 = byteArrayToInt(temp_data_2);
                temp_data.setData_2(data_2);
                byte[] temp_data_3 = new byte[8];
                System.arraycopy(datas, 16, temp_data_3, 0,8);
                double data_3 = bytes2Double(temp_data_3);
                temp_data.setData_3(data_3);
                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return temp_data;
    }
}
