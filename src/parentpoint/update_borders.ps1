$files = Get-ChildItem -Path 'c:\Users\HP\Documents\NetBeansProjects\PARENTPOINT\src\parentpoint' -Recurse -Include *.form,*.java
foreach ($f in $files) {
    $content = Get-Content $f.FullName -Raw
    $newContent = $content -replace '<LineBorder>[\s\S]*?</LineBorder>', '<BevelBorder/>' -replace 'LineBorderInfo', 'BevelBorderInfo' -replace 'BorderFactory\.createLineBorder\(new java\.awt\.Color\([^)]+\)\)', 'BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)'
    if ($content -ne $newContent) {
        Set-Content -Path $f.FullName -Value $newContent -Encoding UTF8
        Write-Host "Updated $($f.FullName)"
    }
}
