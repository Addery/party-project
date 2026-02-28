# 批量更新JPA注解导入语句的PowerShell脚本

# 设置项目根目录
$projectRoot = "h:\dev\project\partyproject\v3\back\src\main\java"

# 查找所有包含javax.persistence导入的Java文件
$javaFiles = Get-ChildItem -Path $projectRoot -Filter "*.java" -Recurse | 
             Select-String -Pattern "import javax\.persistence\." -List | 
             Select-Object -ExpandProperty Path

# 批量替换导入语句
foreach ($file in $javaFiles) {
    Write-Host "正在更新: $file"
    # 读取文件内容
    $content = Get-Content -Path $file -Raw
    # 替换javax.persistence为jakarta.persistence
    $newContent = $content -replace "import javax\.persistence\.([^*]*);", "import jakarta.persistence.$1;"
    $newContent = $newContent -replace "import javax\.persistence\.\*;", "import jakarta.persistence.*;"
    # 写回文件
    Set-Content -Path $file -Value $newContent
}

Write-Host "所有JPA导入语句已更新完成!"
