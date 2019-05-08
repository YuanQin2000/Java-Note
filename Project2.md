# Java Project 2
从手动构建工程(jar包)的过程来看，即使不考虑增量编译的问题，也是非常繁琐的操作。虽然可以使用make或脚本工具来构建Java工程，但更好的方法是使用Java自己的工具来构建项目，按照出现的时间先后顺序，使用最广泛的两种工具是Ant和Maven。这些工具相对于make工程构建方法而言更简单、智能。
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
* XML文件包括两部分内容，第一部分为变量的定义(可选的)，第二部分为执行目标的描述，执行目标可以是多个，并且可以有依赖关系，比如build目标依赖于compile目标，目标的名字可以是任意定义。XML可以描述默认执行的目标，也可以在命令行执行ant时指定目标，这些特性和make是相同的。
* XML文件不指定Java源文件的依赖关系，仅需指定源文件目录和编译后输出的目录。
* 使用Ant制作jar包时，需要设置manifest的各种属性，如Main-Class

下面是一个基本(未指定import package的java class path)的Ant XML文件
```
<?xml version="1.0" encoding="UTF-8" ?>
<project name="MyFirstAnt" default="build" basedir=".">

    <property name="src" value="src"/>
    <property name="dest" value="build"/>
    <property name="jarfile" value="hello-ant.jar"/>

    <target name="init">
        <mkdir dir="${dest}"/>
    </target>

    <target name="compile" depends="init">
        <javac srcdir="${src}" destdir="${dest}"/>
    </target>

    <target name="build" depends="compile">
        <jar jarfile="${jarfile}" basedir="${dest}">
            <manifest>
                <attribute name="Main-Class" value="test.ant.HelloAnt" />
            </manifest>
        </jar>
    </target>

    <target name="clean">
        <delete dir="${dest}" />
        <delete file="${jarfile}" />
    </target>

</project>
```
在project目录下执行命令
```
ant build
```
则可以自动生成hello-ant.jar包.
## 使用Maven构建工程
