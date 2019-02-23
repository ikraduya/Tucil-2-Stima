import java.util.Scanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
  private static class ArrListInt {
    public ArrayList<Integer> data;
    public ArrListInt() {
      data = new ArrayList<Integer>();
    }
  }
  
  private static int n, m, r;

  private static boolean[] visited, trapIsland;
  private static ArrListInt[] jMap = null;
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
      
      jMap = new ArrListInt[n];
      Arrays.setAll(jMap, a -> new ArrListInt());
      for (int i=0; i<m; i++) {
        int asal = reader.nextInt();
        int akhir = reader.nextInt();
        jMap[asal-1].data.add(akhir-1);
      }
      reader.close();
  
      // mark island cluster
      visited = new boolean[n];
      trapIsland = new boolean[n];
      ArrayList<Integer> langkahLog = new ArrayList<>();
      langkahLog.add(r);
      DFS(r-1, langkahLog);
      
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
    visited[p] = true;

    boolean trapped = true;
    for (Integer i : jMap[p].data) {
      trapped = false;
      if (!visited[i]) {
        ll.add(i+1);
        DFS(i, ll);
        ll.remove(ll.size()-1);
      }
    }
    if (trapped) {
      trapIsland[p] = true;
      visited[p] = false;
      String sLangkah = Arrays.toString(ll.toArray());
      if (!trappedMap.containsKey(p+1)) {
        trappedMap.put(p+1, new ArrayList<String>());
      }
      trappedMap.get(p+1).add(sLangkah);
    }
  }
}