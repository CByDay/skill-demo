package cn.example.seckilldemo.utils;

import cn.example.seckilldemo.entity.TUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 生成用户工具类
 *
 * @author: LC
 * @date 2022/3/4 3:29 下午
 * @ClassName: UserUtil
 */
@Slf4j
public class UserUtil {

    public static void main(String[] args) throws Exception {
        creatUser(5000);
    }

    private static void creatUser(int count) throws Exception {
        List<TUser> userList = new ArrayList<>(count);
        // 生成用户
        for (int i = 0; i < count; i++) {
            TUser user = new TUser();
            user.setId(13000000000L + i);
            user.setNickname("admin" + i);
            user.setSalt("1a2b3c4d");
            user.setPassword(MD5Util.inputPassToDBPass("123456", user.getSalt()));
            user.setLoginCount(1);
            user.setRegisterDate(new Date());
            userList.add(user);
        }
        log.info("creat user");
//        // 批量插入
//        Connection conn = getConn();
//        String sql = "insert into t_user(login_count, nickname, salt, password, id) values(?,?,?,?,?)";
//        PreparedStatement pstmt = conn.prepareStatement(sql);
//        System.out.println(userList.size());
//        for (int i = 0; i < userList.size(); i++) {
//            TUser user = userList.get(i);
//            pstmt.setInt(1, user.getLoginCount());
//            pstmt.setString(2, user.getNickname());
//            pstmt.setString(3, user.getSalt());
//            pstmt.setString(4, user.getPassword());
//            pstmt.setLong(5, user.getId());
//            pstmt.addBatch();
//            if (i%500 ==0) {
//                pstmt.executeBatch();
//                pstmt.clearParameters();
//            }
//        }
//        pstmt.executeUpdate();
//        pstmt.clearParameters();
//        conn.close();
        log.info("insert to db");
        // 登录
        String urlString = "http://localhost:8080/login/doLogin";
        File file = new File("C:\\Users\\张小疯\\Desktop\\config.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for (int i = 0; i < userList.size(); i++) {
            TUser user = userList.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "mobile=" + user.getId() + "&password="+MD5Util.inputPassToFromPass("123456");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            ObjectMapper mapper = new ObjectMapper();
            RespBean respBean = mapper.readValue(response, RespBean.class);
            String userTicket = ((String) respBean.getObject());
            log.info("creat userTicket : " + userTicket);
            String row = user.getId() + "," + userTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            log.info("write to file ：" + user.getId());
        }
        raf.close();
        log.info("over");
    }

    // 手搓数据库连接
    private static Connection getConn() throws Exception {
        String url = "jdbc:mysql://localhost:3306/jk_manage?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123456789";
//        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, username, password);
    }

}
