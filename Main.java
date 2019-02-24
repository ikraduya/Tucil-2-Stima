import java.util.Scanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
  private static int n, m, r;

  private static boolean[] visited;
  // private static boolean[] trapIsland;
  private static HashMap<Integer, ArrayList<Integer>> jMap = new HashMap<Integer, ArrayList<Integer>>();
  private static HashMap<Integer, ArrayList<String>> trappedMap = new HashMap<Integer, ArrayList<String>>();

  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println("Usage  : java Main <input> <output>");
      System.out.println("Example: java Main input.txt output.txt");
      System.exit(0);
    }

    // measure execution time
    long startTime = System.nanoTime();
    try {
      Scanner reader = new Scanner(new File(args[0]));
      // jumlah pulau (n), jumlah jembatan(m), posisi (r)
      n = reader.nextInt();
      m = reader.nextInt();
      r = reader.nextInt();
      
      for (int i=0; i<m; i++) {
        int asal = reader.nextInt();
        int akhir = reader.nextInt();
        if (!jMap.containsKey(asal)) {
          jMap.put(asal, new ArrayList<Integer>());
        }
        (jMap.get(asal)).add(akhir);
      }
      reader.close();
  
      // mark island cluster
      visited = new boolean[n];
      // trapIsland = new boolean[n];
      ArrayList<Integer> langkahLog = new ArrayList<>();
      langkahLog.add(r);
      DFS(r, langkahLog);
      
      BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));
      for (Integer island : trappedMap.keySet()) {
        writer.write("Island " + island + "\n");
        for (String langkah : trappedMap.get(island)) {
          writer.write(langkah + "\n");
        }
        writer.write("\n");
      }
      writer.close();
    } catch (IOException e) {
      System.err.println("Error: File input doesn't exist");
    }
    long stopTime = System.nanoTime();
    System.out.println("Elapsed execution time: " + ((stopTime - startTime) / 1000000) + " ms");
  }

  private static void DFS(int p, ArrayList<Integer> ll) {
    visited[p-1] = true;

    boolean trapped = true;
    if (jMap.containsKey(p)) {
      trapped = false;
      for (Integer i : jMap.get(p)) {
        if (!visited[i-1]) {
          ll.add(i);
          DFS(i, ll);
          visited[i-1] = false;
          ll.remove(ll.size()-1);
        }
      }    
    }

    if (trapped) {
      // trapIsland[p-1] = true;
      visited[p-1] = false;
      String sLangkah = Arrays.toString(ll.toArray());
      if (!trappedMap.containsKey(p)) {
        trappedMap.put(p, new ArrayList<String>());
      }
      (trappedMap.get(p)).add(sLangkah);
    }
  }
}