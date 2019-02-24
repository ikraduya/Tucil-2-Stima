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
  private static HashMap<Integer, ArrayList<Integer>> jMap = new HashMap<Integer, ArrayList<Integer>>();
  private static HashMap<Integer, ArrayList<String>> trappedMap = new HashMap<Integer, ArrayList<String>>();

  public static void main(String[] args) {
    // check execution parameters
    if (args.length < 2) {
      System.out.println("Usage  : java Main <input> <output>");
      System.out.println("Example: java Main input.txt output.txt");
      System.exit(0);
    }
    
    try {
      // measure execution time
      long startTime = System.nanoTime();

      Scanner reader = new Scanner(new File(args[0]));
      // jumlah pulau (n), jumlah jembatan(m), posisi (r)
      n = reader.nextInt();
      m = reader.nextInt();
      r = reader.nextInt();
      
      // simpan informasi jembatan
      for (int i=0; i<m; i++) {
        int asal = reader.nextInt();
        int akhir = reader.nextInt();
        if (!jMap.containsKey(asal)) {
          jMap.put(asal, new ArrayList<Integer>());
        }
        (jMap.get(asal)).add(akhir);
      }
      reader.close();
  
      visited = new boolean[n];   // variabel untuk menandakan pulau yang telah dikunjungi
      // inisialisasi log langkah
      ArrayList<Integer> langkahLog = new ArrayList<>();  
      langkahLog.add(r);

      // panggil fungsi DnC
      DnC(r, langkahLog);
      
      // mencetak trap island beserta kemungkinan langkah-langkahnya
      BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));
      for (Integer island : trappedMap.keySet()) {
        writer.write("Island " + island + "\n");
        for (String langkah : trappedMap.get(island)) {
          writer.write(langkah + "\n");
        }
        writer.write("\n");
      }

      // mencetak waktu eksekusi
      long stopTime = System.nanoTime();
      writer.write("Elapsed execution time: " + ((stopTime - startTime) / 1000000) + " ms\n");
      writer.close();
    } catch (IOException e) {
      System.err.println("Error: File input doesn't exist");
    }
  }

  private static void DnC(int p, ArrayList<Integer> ll) {
    // tandai pulau sekarang telah dikunjungi
    visited[p-1] = true;

    boolean trapped = true; // jika tidak terdapat jembatan ke pulau lain, akan tetap bernilai true
    if (jMap.containsKey(p)) {
      trapped = false;
      for (Integer i : jMap.get(p)) {
        if (!visited[i-1]) {
          ll.add(i);
          DnC(i, ll);
          visited[i-1] = false;
          ll.remove(ll.size()-1);
        }
      }
    }

    if (trapped) {
      // simpan log langkah ke variabel enumerasi langkah
      visited[p-1] = false;
      String sLangkah = Arrays.toString(ll.toArray());
      if (!trappedMap.containsKey(p)) {
        trappedMap.put(p, new ArrayList<String>());
      }
      (trappedMap.get(p)).add(sLangkah);
    }
  }
}