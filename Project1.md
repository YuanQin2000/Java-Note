# Java Project 1
## 包与目录
包必须与源代码文件存放的路径相匹配：如有下列HelloAnt.java文件
```
package test.ant;

public class HelloAnt {
  public static void main(String[] args) {
    System.out.println("Hello Ant...");
  }
}
```
假设有src目录为项目源文件的根目录，那么HelloAnt.java需要存放于目录
```
src/test/ant/
```
## 手动编译
在项目源文件的根目录下，执行命令
```
javac HelloAnt.java
```
在该源文件所处相同的目录下生成*HelloAnt*.class字节码文件，如果源文件包含了main函数，则此字节码文件时可执行的，在根目录下执行命令
```
java test.ant.HelloAnt
```
显然，发布软件时并不希望字节码文件和源码文件混在一起，可以使用javac的参数指定编译输出的目标目录：
```
javac src/test/ant/HelloAnt.java -d build
```
于是我们得到以下目录树
```
project/
  src/
    test/
      ant/
        HelloAnt.java
  build/
    test/
      ant/
        HelloAnt.class
```
这样，build下的test已经可以作为一个基本的软件包进行发布了
## jar包
上面手工编译的基本软件包使用起来有很多不方便的地方：
* 用户执行程序时需要指定包含main的那个字节码文件，这是用户不太可能知道的实现细节；
* 用户执行程序时需要到class文件的根目录下进行；
* 如果发行的软件包中包括大量字节码文件，则传输、存储都不太方便。

于是我们有jar包来解决这些问题，jar包是一个（压缩）归档的文件，包含一个manifest文件用于描述main所处的字节码文件，以及其他信息，这个manifest文件位于jar包（解压后）的位置
```
META-INF/
  MANIFEST.MF
```
包含了main class的信息（对于可执行的jar文件而言）
```
Main-Class: test.ant.HelloAnt
```
这样，用户可以在任何路径下执行程序：
```
java -jar /jar/file/path/test.jar
```
### 生成jar包
* 进入我们临时建立的class文件的根目录，即```project/build/```
* 执行命令
  ```
  jar -c -v -file=test.jar --main-class=test.ant.HelloAnt test
  ```
  --main-class的内容会写入manifest文件。
  jar的参数还有很多，请参考其帮助文档。

### 解压jar包
执行命令
```
jar -xvf your-file-name.jar
```
