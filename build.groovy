def srcdir = './src'
def destdir = './bin'
def libdir = './lib'
def versionfile = "version.txt"
def destjar = 'Isser-*.jar'.replace('*', new File("${srcdir}/${versionfile}").text)

def mainClass = 'isser.Isser'
def scriptFile = new File("${srcdir}/${mainClass.replace('.', '/')}.groovy")
if (! scriptFile.canRead()) {
  println "Cannot read script file: '${scriptFile}'"
  System.exit(-1)
}

//-----------------------------------------------------------------------------
new AntBuilder().sequential {
  // タスク定義
  taskdef name: 'groovyc', classname: 'org.codehaus.groovy.ant.Groovyc'

  // destディレクトリのクリーン
  delete dir: destdir
  mkdir dir: destdir

  // コンパイル実行
  groovyc srcdir: srcdir, destdir: destdir, {
    classpath {
      fileset dir: libdir, includes: '*.jar'
    }
    javac source: '1.5', target: '1,5', encoding: 'UTF-8', debug: 'on'
  }
  copy file: "${srcdir}/${versionfile}", todir: destdir, overwrite: true

  // jar作成
  jar destfile: destjar, compress: true, index: true, {
    fileset dir: destdir, includes: '**/*.class'
    fileset dir: srcdir, includes: 'version.txt'
    zipgroupfileset dir: libdir, includes: '*.jar'
    manifest {
      attribute name: 'Main-Class', value: mainClass
    }
  }

  // 作成完了
  echo "Build completed!! Run Isser using: 'java -jar ${destjar}'"
}
