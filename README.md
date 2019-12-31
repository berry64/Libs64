# Libs64 Project page

A lightweight Minecraft Bukkit/Spigot plugins library

this project is almost 100% under development.

### Features:
- Easy-to-use SQL features
    - MYSQL
    - SQLITE
- File Management
    - Saving resources to specific locations
    - one-function-call configuration load/reload
- Translator/I18N (under development)

### How to use
For maven:
simply include this in your pom.xml
```xml
<repositories>
     <repository>
         <id>berry64-repo</id>
         <url>https://raw.github.com/berry64/mvn-repo/master/</url>
     </repository>
</repositories>

<dependencies>
        <dependency>
            <groupId>net.berry64.net.berry64.libs64</groupId>
            <artifactId>Libs64</artifactId>
            <version>2.0-INTERNAL</version>
        </dependency>
</dependencies>
```

*Don't forget to add to plugin.yml dependency as well*
```yml
depends: Libs64
```



if you want to bundle Libs64 inside your jar so other people won't have to download a separate jar, you can use maven shade plugin:
```xml
<build>
    <plugins>
    <plugin>
       <groupId>org.apache.maven.plugins</groupId>
       <artifactId>maven-shade-plugin</artifactId>
       <version>3.2.1</version>
       <configuration>
           <minimizeJar>true</minimizeJar>
       </configuration>
       <executions>
           <execution>
               <phase>package</phase>
               <goals>
                   <goal>shade</goal>
               </goals>
           </execution>
       </executions>
       </plugin>
    </plugins>
</build>
```
---
##Libraries Used/Special Thanks to:
- [sqlite-jdbc](https://github.com/xerial/sqlite-jdbc)
- [c3p0](https://www.mchange.com/projects/c3p0/)
- [mysql-connector-java](https://dev.mysql.com/downloads/connector/j/)
- [spigot api](https://www.spigotmc.org/)
- [objenesis](http://objenesis.org/)
