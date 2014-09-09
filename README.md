[V](http://vclient.org)
=====

A simple modified Minecraft client with a module and event system.

The name is derived from it's initial name, V. However, that name was unfitting for a client made for _Victory_.

building
=======

V is currently using [MCP 903](http://vclient.org/mcp/903.zip). This repository should be pulled into the `src/minecraft` directory of your MCP 903 installation. 

You can generate the class files with MCPs `./recompile.sh` and `./reobfuscate.sh`, or you can export the JAR with Eclipse:

1. File > Export...
2. Select _Runnable JAR File_ under Java
3. Select _Client - Client_ under Launch Configuration
4. Choose destination JAR file
5. Select _Copy required libraries into sub-folder..._
6. Finish

dependencies
======

* [Javassist](http://www.csg.ci.i.u-tokyo.ac.jp/~chiba/javassist/)
* [Reflections](https://code.google.com/p/reflections/)

They should be added to your build path.
