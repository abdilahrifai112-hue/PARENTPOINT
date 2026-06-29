$files = @(
    'ds\dsmainframe.form', 'ds\dsmainframe.java',
    'master\MasterKelas.form', 'master\MasterKelas.java',
    'master\MasterOrangTua.form', 'master\MasterOrangTua.java',
    'master\MasterSiswa.form', 'master\MasterSiswa.java',
    'master\MasterUser.form', 'master\MasterUser.java',
    'report\report.form', 'report\report.java',
    'transaksi\InputKehadiran.form', 'transaksi\InputKehadiran.java'
)
foreach ($f in $files) {
    $path = Join-Path 'c:\Users\HP\Documents\NetBeansProjects\PARENTPOINT\src\parentpoint' $f
    if (Test-Path $path) {
        $content = [System.IO.File]::ReadAllText($path)
        $utf8NoBom = New-Object System.Text.UTF8Encoding($False)
        [System.IO.File]::WriteAllText($path, $content, $utf8NoBom)
        Write-Host "Fixed BOM for $path"
    }
}
