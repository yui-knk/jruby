This branch contains experimental work to customize the Graal compiler for
JRuby, plugging it in via JVMCI.

Currently, core/pom.rb depends on local paths to a JVMCI-capable JDK and
the full Graal jar.

Given updated paths and a clean build of JRuby, you can tell the JDK to
use the JRuby-wrapped Graal compiler with the following command line
(adjusting paths as appropriate):

```
JVMCI_LIB=/usr/lib/jvm/jdk8-jvmci/jre/lib/jvmci
GRAAL_LIB=/usr/lib/jvm/graalvm/lib/graal
jruby -J-XX:+UnlockExperimentalVMOptions -J-XX:+EnableJVMCI -J-XX:+UseJVMCICompiler -J-Djvmci.Compiler=jruby-graal -J-Xbootclasspath/a:$JVMCI_LIB/jvmci-api.jar:$JVMCI_LIB/jvmci-hotspot.jar:$GRAAL_LIB/graal.jar script.rb
```

Additional recommended flags:

Use `-Xfixnum.cache=false` to disable the fixnum cache, which helps keep
fixnumes virtualized.

Use `-Xcompile.invokedynamic` to enable the use of InvokeDynamic,
necessary to get Ruby and Java to inline properly across dynamic calls. 
