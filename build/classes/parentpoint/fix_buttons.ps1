$files = @(
    'LOGIN\LOGIN.form', 'LOGIN\LOGIN.java',
    'ds\dsmainframe.form', 'ds\dsmainframe.java',
    'master\MasterKelas.form', 'master\MasterKelas.java',
    'master\MasterOrangTua.form', 'master\MasterOrangTua.java',
    'master\MasterSiswa.form', 'master\MasterSiswa.java',
    'master\MasterUser.form', 'master\MasterUser.java',
    'report\report.form', 'report\report.java',
    'transaksi\InputKehadiran.form', 'transaksi\InputKehadiran.java'
)

$utf8NoBom = New-Object System.Text.UTF8Encoding($False)

foreach ($f in $files) {
    $path = Join-Path 'c:\Users\HP\Documents\NetBeansProjects\PARENTPOINT\src\parentpoint' $f
    if (Test-Path $path) {
        $content = [System.IO.File]::ReadAllText($path)

        if ($path.EndsWith('.form')) {
            # Remove any custom border we added (LineBorderInfo or BevelBorderInfo)
            $content = $content -replace '(?s)<Property name="border" type="javax\.swing\.border\.Border" editor="org\.netbeans\.modules\.form\.editors2\.BorderEditor">.*?</Property>', ''
            
            # Add contentAreaFilled="false" and opaque="true" to all JButtons
            $content = $content -replace '(<Component class="javax\.swing\.JButton" name="[^"]+">\s*<Properties>)', "`$1`n                <Property name=`"contentAreaFilled`" type=`"boolean`" value=`"false`"/>`n                <Property name=`"opaque`" type=`"boolean`" value=`"true`"/>"
        }
        elseif ($path.EndsWith('.java')) {
            # Remove setBorder
            $content = $content -replace 'jButton\d+\.setBorder\(javax\.swing\.BorderFactory\.(createBevelBorder|createLineBorder)\([^)]*\)\);\s*', ''
            
            # Add setContentAreaFilled and setOpaque right after setText or setBackground
            # We will just add it below setBackground
            $content = $content -replace '(jButton\d+\.setBackground\([^;]+;\s*)', "`$1`$($matches[1].Split('.')[0]).setContentAreaFilled(false);`n        $($matches[1].Split('.')[0]).setOpaque(true);`n        "
            
            # Wait, regex replacement in PS needs to be simpler.
            # Let's just find and replace the border with contentAreaFilled and setOpaque
            $content = $content -replace '(jButton\d+)\.setBorder\(javax\.swing\.BorderFactory\.(createBevelBorder|createLineBorder)\([^)]*\)\);', "`$1.setContentAreaFilled(false);`n        `$1.setOpaque(true);"
        }

        [System.IO.File]::WriteAllText($path, $content, $utf8NoBom)
        Write-Host "Fixed buttons for $path"
    }
}
