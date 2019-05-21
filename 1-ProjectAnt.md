# 1. Java Project - Ant
从手动构建工程(jar包)的过程来看，即使不考虑增量编译的问题，也是非常繁琐的操作。虽然可以使用make或脚本工具来构建Java工程，但更好的方法是使用Java自己的工具来构建项目，按照出现的时间先后顺序，使用最广泛的三种工具是Ant, Maven和Gradle。这些工具相对于make工程构建方法而言更简单、智能。
## 使用Ant构建工程
Ant是Apache基金会的一个项目: [https://ant.apache.org/](https://ant.apache.org/)
Ant工具的设计思路和make几乎是相同的，如果熟悉Makefile，使用Ant是非常简单的事情。

* Ant使用XML来表示构建的规则，默认文件为build.xml，此文件需处于工程的根目录上，即
  ```
  project/
    build.xml
    src/
      test/
        ant/
          HelloAnt.java
  ```
* XML文件包括两部分内容，第一部分为属性的定义(可选的)，第二部分为执行目标的描述，执行目标可以是多个，并且可以有依赖关系，比如build目标依赖于compile目标，目标的名字可以是任意定义。XML可以描述默认执行的目标，也可以在命令行执行ant时指定目标，这些特性和make是相同的。
* Ant内部实现了对源文件的依赖分析，因此在XML文件中不需要指定Java源文件的依赖关系，仅指定源文件目录和编译后输出的目录。
* 使用Ant制作jar包时，需要设置manifest的各种属性，如Main-Class

下面是一个基本的Ant XML文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<project name="MyFirstAnt" default="build" basedir=".">

    <property name="src-dir" value="src"/>
    <property name="dest-dir" value="build"/>
    <property name="target-jar" value="hello-ant.jar"/>

    <target name="init">
        <mkdir dir="${dest-dir}"/>
    </target>

    <target name="compile" depends="init">
        <javac srcdir="${src-dir}" destdir="${dest-dir}"  includeantruntime="false"/>
    </target>

    <target name="build" depends="compile">
        <jar jarfile="${target-jar}" basedir="${dest-dir}">
            <manifest>
                <attribute name="Main-Class" value="test.ant.HelloAnt" />
            </manifest>
        </jar>
    </target>

    <target name="clean">
        <delete dir="${dest-dir}" />
        <delete file="${target-jar}" />
    </target>

</project>
```
在project目录下执行命令
```
ant build
```
则可以自动生成hello-ant.jar包.

## 使用其他Java程序库
使用其他Java程序库需要两个配置：
* 在某个目录下存放要使用的Java库文件，通常为jar文件；
* 指定编译时的class path指向上述的Java库文件。

通过环境变量JAVA_HOME的配置，Java程序默认使用JDK/JRE自带的系统库。如果要使用其他目录下的Java库，则需要在build文件显式设置class path。

我们在工程下面增加一个lib目录，用于存放要引用的Java库，假设我们要引用一个名叫hello-bar.jar的文件，则有如下的工程目录结构

```
  project/
    build.xml
    lib/
        hello-bar.jar
    src/
      test/
        ant/
          HelloAnt.java
```
对应的build.xml对应如下:
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<project name="MyFirstAnt" default="build" basedir=".">

    <property name="src-dir" value="src"/>
    <property name="dest-dir" value="build"/>
    <property name="lib-dir" value="lib"/>
    <property name="target-jar" value="hello-ant.jar"/>

    <fileset id="lib-jar-files" dir="${lib-dir}">
        <include name="**/*.jar"/>
    </fileset>

    <path id="lib-class-path">
        <fileset refid="lib-jar-files">
        </fileset>
    </path>

    <target name="init">
        <mkdir dir="${dest-dir}"/>
    </target>

    <target name="compile" depends="init">
        <javac srcdir="${src-dir}" destdir="${dest-dir}" classpathref="lib-class-path" includeantruntime="false"/>
    </target>

    <target name="build" depends="compile">
        <jar jarfile="${target-jar}" basedir="${dest-dir}">
            <manifest>
                <attribute name="Main-Class" value="test.ant.HelloAnt" />
            </manifest>
            <zipgroupfileset refid="lib-jar-files"/>
        </jar>
    </target>

    <target name="clean">
        <delete dir="${dest-dir}" />
        <delete file="${target-jar}" />
    </target>

</project>
```
在上面的build.xml中，除了增加class path的设置，还在build->jar下增加了
```xml
<zipgroupfileset refid="lib-jar-files"/>
```
这样我们生成的目标hello-ant.jar文件将打包所引用的Java库文件。
对第三方Java库文件的处理有两种选择：

* 将第三方库文件打包进自己要发布的jar文件，优点是用户部署简单，不用手动下载依赖的jar文件；缺点是如果要更新第三方库，需要重新发布自己的软件包，增大了软件包大小(如果系统中以后该软件库，则存在磁盘空间的浪费)
* 让用户自己维护第三方库，优点是方便第三库更新，减少软件包容量；缺点是无法管理依赖软件包的版本，用户的升级可能导致程序不可用

对第三方Java库的处理，暴露了Ant工具的缺点，这也正是Maven要解决的问题。