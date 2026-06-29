import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class FixJavaFiles {
    public static void main(String[] args) throws Exception {
        String baseDir = "c:/Users/HP/Documents/NetBeansProjects/PARENTPOINT/src/parentpoint";
        String[] files = {
            "LOGIN/LOGIN.java",
            "ds/dsmainframe.java",
            "master/MasterKelas.java",
            "master/MasterOrangTua.java",
            "master/MasterSiswa.java",
            "master/MasterUser.java",
            "report/report.java",
            "transaksi/InputKehadiran.java"
        };
        
        for (String f : files) {
            File file = new File(baseDir, f);
            if (!file.exists()) continue;
            
            String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            
            // Fix the corrupted lines:
            // $([1].Split('.')[0]).setContentAreaFilled(false);
            // .setOpaque(true);
            // This is completely broken. Let's remove them.
            content = content.replaceAll("\\$\\(.*setContentAreaFilled\\(false\\);\\s*\\.setOpaque\\(true\\);\\s*", "");
            
            // Let's also find all jButtonX declarations and add setContentAreaFilled/setOpaque correctly if they are missing
            // Wait, we don't even need to add it to .java!
            // NetBeans generates the .java based on the .form!
            // If I just open NetBeans, it will rewrite the .java anyway!
            // But to make it compile, I just need to remove the corrupted lines.
            
            Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
            System.out.println("Fixed " + f);
        }
    }
}
