# Copied Downloads Remover

Small tool that deletes files/directories in the Downloads folder if they already occur somewhere else in the user's home folder. Made for those that tend to copy things out of their Downloads folder without removing the original file.

How to use:
1) Clone the repository
2) Open a terminal and navigate to the src folder
3) Compile the .java source files into .class files
'''bash
  javac Main.java
'''
4) Build the .class files into a jar artifact
'''bash
jar cvfe DownloadsClearer.jar Main *.class
'''
5) Making the jar runnable (unix basic OS)
'''bash
chmod +x DownloadsClearer.jar
'''
6) Running the jar:
'''bash
java -jar DownloadsClearer.jar
'''
