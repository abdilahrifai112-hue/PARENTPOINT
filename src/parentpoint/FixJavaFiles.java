import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class FixJavaFiles {
    public static void main(String[] args) throws Exception {
        List<Path> files = Files.walk(Paths.get("src/parentpoint"))
                                .filter(Files::isRegularFile)
                                .filter(p -> p.toString().endsWith(".java"))
                                .collect(Collectors.toList());

        for (Path file : files) {
            String pathStr = file.toString().replace("\\", "/");
            if (pathStr.contains("LOGIN.java") || pathStr.contains("koneksi.java") || pathStr.contains("DesignUtil.java") || pathStr.contains("Session.java")) {
                continue;
            }
            
            String content = new String(Files.readAllBytes(file));
            
            // Only inject if it has initComponents(); and doesn't already have setExtendedState
            if (content.contains("initComponents();") && !content.contains("setExtendedState")) {
                content = content.replace("initComponents();", "initComponents();\n        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);");
                Files.write(file, content.getBytes());
                System.out.println("Updated: " + file);
            }
        }
    }
}
