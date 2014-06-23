package ua.daniela;

import java.io.*;
import java.util.*;

/**
 * Created by zidd on 4/12/14.
 */
public class Dictionary {
    private Map<String, Set<String>> words;
    private Map<String, Set<String>> reverseWords;
    private AddedWordList addedWordList;

    private String dictionaryFilePath;

    public Dictionary(String dictionaryFilePath) {
        this.dictionaryFilePath = dictionaryFilePath;
        words = new HashMap<String, Set<String>>();
        reverseWords = new HashMap<String, Set<String>>();
        addedWordList = new AddedWordList();
        readWordsFromFile(dictionaryFilePath);
    }

    public String getDictionaryFilePath() {
        return dictionaryFilePath;
    }

    public Set<String> getTranslated(String word) {
        return words.get(word.toLowerCase());
    }

    public Set<String> getReverseTranslated(String word) {
        return reverseWords.get(word.toLowerCase());
    }

    private void readWordsFromFile(String dictionaryFilePath) {
        Scanner sc = null;
        FileInputStream inputStream = null;
        try {
            String str = null;
            inputStream = new FileInputStream(dictionaryFilePath);
            sc = new Scanner(inputStream, "UTF-8");
            sc.nextLine();//пропускаем первую строчку, если начать писать с первой строки, то повторяющиеся слова могут получить разный хешкод(
            sc.useDelimiter("[|]|\r\n");//используем в кавчестве разделителя | и конец строки, что позволяет нам читать целые выражения с пробелами
            while( sc.hasNext()) {
                String word = sc.next().toLowerCase();
                String translated = sc.next().toLowerCase();
                addWord(word, translated);
                addReverseWord(translated, word);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addWord(String word, String translated) {
        if( !words.containsKey(word) ) {
            Set<String> analogWordsList = new HashSet<String>();
            analogWordsList.add(translated);
            words.put(word, analogWordsList);

        } else {
            Set<String> analogWordsList = words.get(word);
            analogWordsList.add(translated);
        }
    }

    private void addReverseWord(String word, String translated) {
        if( !reverseWords.containsKey(word) ) {
            Set<String> analogWordsList = new HashSet<String>();
            analogWordsList.add(translated);
            reverseWords.put(word, analogWordsList);

        } else {
            Set<String> analogWordsList = reverseWords.get(word);
            analogWordsList.add(translated);
        }
    }

    public void addWordToAddedList(String word, String translated) {
        addedWordList.addWord(word.toLowerCase(), translated.toLowerCase());
    }

    public Set<String> getAddedWord(String word) {
        return addedWordList.get(word.toLowerCase());
    }

    public static class AddedWordList {
        private Map<String,Set<String>> words;
        private Map<String,Set<String>> reverseWords;
        private Set<String> wordsSet;

        public Set<String> getWordsSet() {
            return wordsSet;
        }

        public AddedWordList() {
            words = new HashMap<String, Set<String>>();
            reverseWords = new HashMap<String, Set<String>>();
            wordsSet = new HashSet<String>();
        }

        public void addWord(String word, String translated){
            word = word.toLowerCase();
            translated = translated.toLowerCase();
            wordsSet.add(word + "|" + translated);

            if( !words.containsKey(word) ) {
                Set<String> analogWordsList = new HashSet<String>();
                analogWordsList.add(translated);
                words.put(word, analogWordsList);

            } else {
                Set<String> analogWordsList = words.get(word);
                analogWordsList.add(translated);
            }
            if( !reverseWords.containsKey(translated) ) {
                Set<String> analogWordsList = new HashSet<String>();
                analogWordsList.add(word);
                reverseWords.put(translated, analogWordsList);

            } else {
                Set<String> analogWordsList = reverseWords.get(translated);
                analogWordsList.add(word);
            }
        }

        private Set<String> getWord(String word) {
            return words.get(word.toLowerCase());
        }

        private Set<String> getTranslatedWord(String word) {
            return reverseWords.get(word.toLowerCase());
        }

        public Set<String> get(String word) {
            Set<String> result = getWord(word);
            if(result == null) {
                result = getTranslatedWord(word);
            }
            return result;

        }
    }

    public void saveAddedWordInFile() {
        FileOutputStream outputStream = null;
        OutputStreamWriter streamWriter = null;
        try {
            outputStream = new FileOutputStream(dictionaryFilePath, true);
            streamWriter = new OutputStreamWriter(outputStream,"UTF-8");
            for(String str : addedWordList.getWordsSet()) {
                streamWriter.write(str + "\r\n");
                streamWriter.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                streamWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
