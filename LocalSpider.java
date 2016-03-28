package mypackage;
import java.util.*;
import java.util.Map.Entry;
import java.io.*;
 
public class LocalSpider {
 
  private ThreadQueue workQ;
  static int i = 0;
  static Map<String,String> mymap=new  HashMap<String,String>();
 
 private class Worker implements Runnable {
 
  private ThreadQueue queue;
 
  public Worker(ThreadQueue q) {
   queue = q;
  }
 
//  since main thread has placed all directories into the workQ, we
//  know that all of them are legal directories; therefore, do not need
//  to try ... catch in the while loop below
 
  public void run() {
   String name;
   while ((name = queue.remove()) != null) {
    File file = new File(name);
    String entries[] = file.list();
    if (entries == null)
     continue;
    for (String entry : entries) {
     if (entry.compareTo(".") == 0)
      continue;
     if (entry.compareTo("..") == 0)
      continue;
     String fn = name + "\\" + entry;
     System.out.println(fn);
    
    }
    try {
		searchfile();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    writefilename(mymap);
   }
   
   
   
   
 //  writefilename(mymap);
   
  }
 }
 
 public LocalSpider() {
  workQ = new ThreadQueue();
 }
 
 public Worker createWorker() {
  return new Worker(workQ);
 }
 
 
// need try ... catch below in case the directory is not legal
 
 public void processDirectory(String dir) {
   try{
   File file = new File(dir);
   if (file.isDirectory()) {
	   /*if(file.listFiles()!=null){
		  // Readingafile(file);
		   
	   }*/
	  // Readingafile(file);
    String entries[] = file.list();
    if (entries != null)
     workQ.add(dir);
 
    for (String entry : entries) {
     String subdir;
     if (entry.compareTo(".") == 0)
      continue;
     if (entry.compareTo("..") == 0)
      continue;
     if (dir.endsWith("\\"))
      subdir = dir+entry;
     else
      subdir = dir+"\\"+entry;
     
     //Readingafile(entry);
     
     
     
     mymap.put(entry, subdir);
   
     processDirectory(subdir);
     

    }
   }
   
   
   }catch(Exception e){}
 }
 
 
public void Readingafile(File file) throws FileNotFoundException{
	 
	 System.out.println(file.getName());
	 File toread=new File(file.getName());
	 String name=file.getName();
	 String extension=name.substring(name.lastIndexOf(".")+1);
	 if(extension.compareTo("txt")==0){
	 FileInputStream fis=new FileInputStream(toread);
	 Scanner sc=new Scanner(fis);
	 String currentline;
	 while(sc.hasNextLine()){
		 currentline=sc.nextLine();
		 System.out.println(currentline);
	 }
	 }
	 
 }
 
 public void writefilename(Map<String,String> map){
	 
	 try {
		 
		File filename=new File("G://value.txt");
		FileOutputStream fus=new FileOutputStream(filename);
		PrintWriter pw=new PrintWriter(fus);
		
		for(Map.Entry<String, String> m :map.entrySet()){
			pw.println(m.getKey()+"="+m.getValue());
			
		}
		pw.flush();
		pw.close();
		fus.close();
		//ous.writeObject(map.toString());
		//ous.close();
		fus.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
 }
 
 
 public void searchfile() throws FileNotFoundException{
	 
	 System.out.println("enter the file name");
	   Scanner i=new Scanner(System.in);
	   String inputfilename;
	   inputfilename=i.nextLine();
	   
	 
	 for (Entry<String, String> e : mymap.entrySet()) {
		 if (e.getKey().equalsIgnoreCase(inputfilename)) {
		    System.out.println("File Found " + e.getKey()+" " +e.getValue());
		   }
		 }
	 
	 
	 
	 
 }
 public static void main(String Args[]) {
 
  LocalSpider fc = new LocalSpider();
 
 
  int N = 3;
  ArrayList<Thread> thread = new ArrayList<Thread>(N);
  for (int i = 0; i < N; i++) {
   Thread t = new Thread(fc.createWorker());
   thread.add(t);
   t.start();
  }
 
//  now place each directory into the workQ
 
  fc.processDirectory("G://Spider");
 
//  indicate that there are no more directories to add
 
  fc.workQ.finish();
 
  for (int i = 0; i < N; i++){
   try {
    thread.get(i).join();
   } catch (Exception e) {};
  }
  
  
  //fc.searchfile("a.txt");
  
  
 }
}