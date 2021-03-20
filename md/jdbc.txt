mysql与oracle性能测试(1)
package sql;
 
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
 
public class DataTest {
    public static void main(String[] args) throws Exception {
        test1();
    }
 
    /**
     * 环境:windows xp 32位,内存1G, mysql(5.5.20,新安装,默认环境),oracle(10g)
 
 mysql:
     * 插入数据:50000,用时75730 失败一次 插入数据:100000,用时169615 插入数据:150000,用时247524
     * 插入数据:200000,用时329131 插入数据:250000,用时424650 失败一次 插入数据:300000,用时544870
     * 插入数据:350000,用时648177 插入数据:400000,用时760059 失败一次 插入数据:450000,用时900742
     * 插入数据:500000,用时1042981 <br>
     *
 
oracle:插入数据:50000,用时4951 失败一次 插入数据:100000,用时10819 插入数据:150000,用时15122
     * 插入数据:200000,用时18222 插入数据:250000,用时21752 失败一次 插入数据:300000,用时25093
     * 插入数据:350000,用时29586 插入数据:400000,用时35261 失败一次 插入数据:450000,用时40396
     * 插入数据:500000,用时64483 插入数据:550000,用时77217 插入数据:600000,用时88078
     * 插入数据:650000,用时91301 失败一次 插入数据:700000,用时95984 插入数据:750000,用时113646
     * 插入数据:800000,用时119063 插入数据:850000,用时123731 插入数据:900000,用时143562
     * 插入数据:950000,用时148299 插入数据:1000000,用时154123
     *
     * @throws Exception
     */
    public static void test1() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream("d:/data/test.txt"), "UTF-8"));
        // Class.forName("com.mysql.jdbc.Driver");
        // Connection conn = DriverManager.getConnection(
        // "jdbc:mysql://localhost:3308/test", "test", "123456");
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:orcl", "scott", "tiger");
        Statement st = conn.createStatement();
        st.execute("create table usertest(ta varchar(50) primary key,tb varchar(255),tc varchar(255))");
        st.close();
        conn.setAutoCommit(false);
        PreparedStatement ps = conn
                .prepareStatement("insert into usertest(ta,tb,tc) values(?,?,?)");
        int numb = 0;
        String str = null;
        long l = System.currentTimeMillis();
        while ((str = br.readLine()) != null) {
            String[] record = str.split(" # ");
            if (record.length != 3) {
                continue;
            }
            ps.setString(1, record[0]);
            ps.setString(2, record[1]);
            ps.setString(3, record[2]);
            ps.addBatch();
            numb++;
            if (numb % 50000 == 0) {
                try {
                    ps.executeBatch();
                    conn.commit();
                    ps.clearBatch();
                    System.out.println("插入数据:" + numb + ",用时"
                            + (System.currentTimeMillis() - l));
                } catch (Exception e) {
                    numb -= 10000;
                    System.out.println("************失败一次");
                }
            }
        }
        ps.executeBatch();
        conn.commit();
        System.out.println("插入数据:" + numb + ",用时"
                + (System.currentTimeMillis() - l));
        br.close();
        ps.close();
        conn.close();
    }
}


mysql与oracle性能测试(2)
/**
 * 环境:windows xp 32位,内存1G, mysql(5.5.20,新安装,默认环境),oracle(10g) <br>
 * <br>
 * mysql:插入数据:10000,用时40115 插入数据:20000,用时78592 插入数据:30000,用时115268
 * 插入数据:40000,用时150207 插入数据:50000,用时185534 插入数据:60000,用时220459
 * 插入数据:70000,用时255586 插入数据:80000,用时290891 插入数据:90000,用时325655<br>
 * <br>
 * oracle:插入数据:10000,用时52422 插入数据:20000,用时104468 插入数据:30000,用时155523
 * 插入数据:40000,用时206551 插入数据:50000,用时257419 插入数据:60000,用时309063
 * 插入数据:70000,用时364110 插入数据:80000,用时419607 插入数据:90000,用时473568
 *
 * @throws Exception
 */
public static void test2() throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(
            new FileInputStream("d:/data/test.txt"), "UTF-8"));
    // Class.forName("com.mysql.jdbc.Driver");
    // Connection conn = DriverManager.getConnection(
    // "jdbc:mysql://localhost:3308/test", "test", "123456");
    Class.forName("oracle.jdbc.driver.OracleDriver");
    Connection conn = DriverManager.getConnection(
            "jdbc:oracle:thin:@localhost:1521:orcl", "scott", "tiger");
    Statement st = conn.createStatement();
    st.execute("create table usertest(ta varchar(50) primary key,tb varchar(255),tc varchar(255))");
    st.close();
    PreparedStatement ps = conn
            .prepareStatement("insert into usertest(ta,tb,tc) values(?,?,?)");
    int numb = 0;
    String str = null;
    long l = System.currentTimeMillis();
    while ((str = br.readLine()) != null) {
        String[] record = str.split(" # ");
        if (record.length != 3) {
            continue;
        }
        ps.setString(1, record[0]);
        ps.setString(2, record[1]);
        ps.setString(3, record[2]);
        ps.execute();
        numb++;
        if (numb % 10000 == 0) {
            System.out.println("插入数据:" + numb + ",用时"
                    + (System.currentTimeMillis() - l));
        }
    }
    System.out.println("插入数据:" + numb + ",用时"
            + (System.currentTimeMillis() - l));
    br.close();
    ps.close();
    conn.close();
}
