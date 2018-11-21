## Maven & Gradle 本地仓库清理工具

### 综述
Maven及Gradle在使用过程中会由于自动加载、网络等各种原因会产生大量无用文件，极度占用磁盘空间，对于强迫症患者来说，这简直不能忍。
因此决定写一个小工具来清理这些垃圾。

### 无用文件
- 带有`lastUpdated`字符的文件
- 带有`unknown`字符的文件及目录
- `error`目录

### 运行环境
- JAVA8