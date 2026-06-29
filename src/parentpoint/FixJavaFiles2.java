import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class FixJavaFiles2 {
    public static void main(String[] args) throws Exception {
        List<Path> files = Files.walk(Paths.get("src/parentpoint"))
                                .filter(Files::isRegularFile)
                                .filter(p -> p.toString().endsWith(".java"))
                                .collect(Collectors.toList());

        for (Path file : files) {
            String pathStr = file.toString().replace("\\", "/");
            if (pathStr.contains("LOGIN.java") || pathStr.contains("koneksi.java") || pathStr.contains("DesignUtil.java") || pathStr.contains("Session.java") || pathStr.contains("FixJavaFiles")) {
                continue;
            }
            
            String content = new String(Files.readAllBytes(file));
            boolean changed = false;
            
            // Remove setSize calls
            if (content.contains("setSize(")) {
                content = content.replaceAll("(?m)^\\s*setSize\\(.*?\\);\\s*$", "// setSize removed for responsiveness");
                changed = true;
            }
            
            // Remove any pack() calls that might be outside of Netbeans generated code
            // Actually pack() is in generated code which we shouldn't touch with regex blindly, 
            // but we can put setExtendedState AFTER styleComponents();
            
            // Inject setExtendedState at the end of the constructor
            // A typical constructor ends with '}'
            // Let's just replace "loadData();" with "loadData(); this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);"
            // Or better, just add it to styleComponents
            if (content.contains("styleComponents() {")) {
                content = content.replace("styleComponents() {", "styleComponents() {\n        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);");
                changed = true;
            }

            if (changed) {
                Files.write(file, content.getBytes());
                System.out.println("Updated layout in: " + file);
            }
        }
    }
}
