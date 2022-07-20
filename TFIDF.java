import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TFIDF {
  //[Calculates the TF]
    public double tf(List<String> document, String term) {
        double result = 0;
        for (String word : document) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result / document.size();
    }

  //[Calculates the DF]
    public int df(List<List<String>> documents, String term) {
        int n = 0;
        if (term != null && !"".equals(term)) {
            for (List<String> document : documents) {
                for (String word : document) {
                    if (term.equalsIgnoreCase(word)) {
                        n++;
                        break;
                    }
                }
            }
        } else {
            System.out.println(" term cannot be null or empty string ");
        }
        return n;
    }

   //[Calculates the IDF]
    public double idf(List<List<String>> documents, String term) {
        return Math.log(documents.size() / df(documents, term) + 1);
    }

  //[Calculates the TFIDF]
    public double tfIdf(List<String> document, List<List<String>> documents, String term) {
        return tf(document, term) * idf(documents, term);
    }


   //  [File Reader]
    private static List<String> readAndGetWords(String filePath) {
        List<String> lstWords = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(line -> {
                String[] arrSen = line.split(",");    //[Splits the lines from the file by line to an array list of strings.]
                for (String sen : arrSen) {
                    String[] arrWords = sen.trim().split(" ");
                    for (String w : arrWords) {
                        lstWords.add(w.trim());
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lstWords;
    }
  //[Records the documents into a list of strings]
    public static void main(String[] args) {
        List<List<String>> documents = new ArrayList<>();
        for (String filePath : args) {
            documents.add(readAndGetWords(filePath));
        }
        
      //[Use Calculations for the desired word(s)]
        TFIDF tfIdf = new TFIDF();  
        System.out.println("[fox] word frequency in document1:" + tfIdf.tf(documents.get(0), "fox"));
        System.out.println("[fox] word frequency in document2:" + tfIdf.tf(documents.get(1), "fox"));
        System.out.println("[fox] word frequency in document3:" + tfIdf.tf(documents.get(2), "fox"));
        System.out.println("[fox] Word frequency in the document set:" + tfIdf.df(documents, "fox"));
        System.out.println("[fox]'s if-idf algorithm " + tfIdf.tfIdf(documents.get(1), documents, "fox"));
        System.out.println("[brown]'s if-idf algorithm:" + tfIdf.tfIdf(documents.get(1), documents, "brown"));
        System.out.println("[brown] Word frequency in the document set:" + tfIdf.df(documents, "brown"));

    }
}