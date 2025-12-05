# 1. 完整打包（包含分离的依赖）
mvn clean package

# 2. 跳过测试打包
mvn clean package -DskipTests

# 3. 只生成瘦JAR和lib目录
mvn clean dependency:copy-dependencies package -DskipTests

# 4. 生成完整发布包（ZIP格式）
mvn clean package assembly:single -DskipTests

# 5. 查看生成的目录结构
tree target/your-app-package/