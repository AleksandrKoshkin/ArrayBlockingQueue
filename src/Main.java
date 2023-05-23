import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static BlockingQueue<String> arrayBlockingQueueA = new ArrayBlockingQueue(100);
    public static BlockingQueue<String> arrayBlockingQueueB = new ArrayBlockingQueue(100);
    public static BlockingQueue<String> arrayBlockingQueueC = new ArrayBlockingQueue(100);
    static int maxA = 0;
    static int maxB = 0;
    static int maxC = 0;

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int search(char s, String text, int max) {
        int counter = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == s) {
                counter++;
            }
        }
        if (counter > max) {
            return counter;
        } else {
            return max;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        Thread thread = new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                texts[i] = generateText("abc", 3 + random.nextInt(3));
                try {
                    arrayBlockingQueueA.put(texts[i]);
                    arrayBlockingQueueB.put(texts[i]);
                    arrayBlockingQueueC.put(texts[i]);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
        Thread threadA = new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                try {
                    maxA = search('a', arrayBlockingQueueA.take(), maxA);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        threadA.start();
        Thread threadB = new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                try {
                    maxB = search('b', arrayBlockingQueueB.take(), maxB);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        threadB.start();
        Thread threadC = new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                try {
                    maxC = search('c', arrayBlockingQueueC.take(), maxC);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        threadC.start();
        threadA.join();
        threadB.join();
        threadC.join();
        System.out.println("Максимум а " + maxA + "\n" +
                "Максимум b " + maxB + "\n" +
                "Максимум c " + maxC);
    }
}