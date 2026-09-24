package com.karmorak.lib.prototype;
//v2.0
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.karmorak.lib.KLIB;
import com.karmorak.lib.utils.file.FileUtils;

import static com.karmorak.lib.utils.file.FileUtils.WRITE_MODE.*;

public class Config {

    //TODO config could use the caching instead of every time rescan the file, implement as optional parameter

    private final String PATH;
    private final File FILE;

    private ArrayList<String> file_contents;

    private static final String SEPARATOR_VALUE = ": ";
//	private static final String SEPARATOR_BRACKET = ": {";

    public Config(String path) {
        PATH = path;
        FILE = new File(path);

        FileUtils.checkFile(FILE, true);
    }

    ArrayList<String> readConfig() {
        return file_contents = FileUtils.readFile(FILE);
    }


    public ArrayList<String> getKeys() {
        readConfig();

        ArrayList<String> keys = new ArrayList<>();

        for (String fileContent : file_contents) {
            String content = fileContent.split(": ")[0];
            keys.add(content);
        }

        return keys;
    }

    //**return if theres is already a value set
    public static boolean isValue(String key, String line) {
        if (line.startsWith(key)) {
            String content = line.replaceFirst(key + SEPARATOR_VALUE, "");
            return !content.isEmpty();
        }
        return false;
    }

    public static boolean isBracketValue(String key, String line) {
        if (line.startsWith(key + SEPARATOR_VALUE + "{")) {
            String content = line.replaceFirst(key + SEPARATOR_VALUE + "\\{", "");
            return !content.isEmpty();
        }
        return false;
    }

    public String getString(String key) {

        String[] value = getValue_fromData(key, readConfig());
        if (value != null && value.length > 0) {
            return value[0];
        }
        return null;
    }

    public String getString(String key, String def_value) {
        String result = getString(key);
        if (result == null) {
            set(key, def_value);
            return def_value;
        } else {
            return result;
        }
    }

    public String[] getStrings(String key) {
        return getValue_fromData(key, readConfig());
    }

    public String[] getStrings(String key, String[] def_value) {
        String[] value = getValue_fromData(key, readConfig());
        if (value != null)
            return value;
        else {
            set(key, def_value);
            return def_value;
        }
    }

    public void set(String key, String value) {
        int found = -1;

        for (int i = 0; i < file_contents.size(); i++) {
            String line = file_contents.get(i);
            if (line.startsWith(key)) {
                found = i;
                break;
            }
        }

        try {
            FileUtils.writeToFile(FILE, key + SEPARATOR_VALUE + value, REPLACE_FIRST_LINE, found);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String key, String[] value) {
        readConfig();
        if (value == null) return;

        int line_open = -1;
        int line_close = -1;
        int open = -1;
        int close = -1;

        String[] keys;
        if (key.contains(".")) keys = key.split("\\.");
        else keys = new String[]{key};

        for (int i = 0; i < file_contents.size(); i++) {
            String line = file_contents.get(i);
            boolean ignore = line.startsWith("//");


            if (line_open == -1 && line.replace("\t", "").startsWith(keys[keys.length - 1])) {
                if (line.replace("\t", "").startsWith(keys[keys.length - 1] + SEPARATOR_VALUE + "{")) {
                    line_open = i;
                } else if (value.length == 0) {
                    try {
                        FileUtils.writeToFile(FILE, key + SEPARATOR_VALUE, REPLACE_FIRST_LINE, i);
                    } catch (IOException e) {
                        KLIB.log(3, "Failed to write config file");
                        e.printStackTrace();
                    }
                } else if (value.length == 1) {
                    try {
                        FileUtils.writeToFile(FILE, key + SEPARATOR_VALUE + value[0], REPLACE_FIRST_LINE, i);
                    } catch (IOException e) {
                        KLIB.log(3, "Failed to write config file");
                        e.printStackTrace();
                    }
                } else {
                    line_open = i;
                    line_close = i;
                    break;
                }
            }

            if (line_open >= 0) {
                for (char c : line.toCharArray()) {
                    if (c == '"') {
                        ignore = !ignore;
                    }
                    if (!ignore) {
                        if (c == '{') {
                            open++;
                        } else if (c == '}') {
                            close++;
                        }
                        if (close != -1 && open == close) {
                            line_close = i;
                            i = file_contents.size();
                            break;
                        }
                    }
                }
            }
        }


        //Tabs setzen0
        String tabs = "\t".repeat(keys.length);
        for (int i = 0; i < value.length; i++) {
            value[i] = tabs + value[i];
        }

        //neue Datei bauen
        ArrayList<String> newFile_contents = new ArrayList<>();
        for (int i = 0; i < file_contents.size(); i++) {
            if (i < line_open)
                newFile_contents.add(file_contents.get(i));
            else if (i == line_open) {
                newFile_contents.add(tabs.replaceFirst("\t", "") + keys[keys.length - 1] + ": {");
                newFile_contents.addAll(Arrays.asList(value));
                newFile_contents.add(tabs.replaceFirst("\t", "") + "}");
            } else {
                if (i > line_close) {
                    newFile_contents.add(file_contents.get(i));
                }
            }
        }

        //Schreiben
        try {
            FileUtils.writeToFile(FILE, newFile_contents, REPLACE_FILE, -1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void set(String name, int[] value) {
        set(name, Arrays.toString(value));
    }


    public String[] get(String key, String[] def_value) {
        if (def_value == null) return new String[]{};
        if (def_value.length == 0) {
            set(key, def_value);
            return new String[]{};
        }
        return getStrings(key, def_value);
    }

    public boolean getBoolean(String name) {
        String c = getString(name);
        if (c.equals("0"))
            return false;
        if (c.equals("1"))
            return true;
        return Boolean.parseBoolean(c);
    }

    public boolean getBoolean(String name, boolean def_value) {
        String c = getString(name, "" + def_value);
        if (c.equals("0"))
            return false;
        if (c.equals("1"))
            return true;
        return Boolean.parseBoolean(c);
    }

    public int getInteger(String name) {
        return Integer.parseInt(getString(name));
    }

    public int getInteger(String name, int def_value) {
        return Integer.parseInt(getString(name, "" + def_value));
    }

    public Double getDouble(String name) {
        return Double.parseDouble(getString(name));
    }

    public double getDouble(String name, double def_value) {
        return Double.parseDouble(getString(name, "" + def_value));
    }

    public float getFloat(String name) {
        return Float.parseFloat(getString(name));
    }

    public float getFloat(String name, float def_value) {
        return Float.parseFloat(getString(name, "" + def_value));
    }


    public String[] getStringArray(String key) {
        String value = getString(key);
        value = value.substring(1, value.length() - 1);

        return value.split(", ");
    }

    public String[] getStringArray(String key, String[] def_value) {
        String value = getString(key, "[" + String.join(", ", def_value) + "];");
        value = value.substring(1, value.length() - 1);

        return value.split(", ");
    }

    public int[] getIntArray(String key) {

        String value = getString(key);
        if (value == null || value.isEmpty()) return null;
        value = value.substring(1, value.length() - 1);

        String[] list = value.split(", ");
        int[] list_int = new int[list.length];
        for (int i = 0; i < list.length; i++) {
            try {
                list_int[i] = Integer.parseInt(list[i]);
            } catch (NumberFormatException e) {
                System.err.println(list[i] + " is not an integer");
                return null;
            }
        }
        return list_int;
    }

    public int[] getIntArray(String key, int[] def_value) {

        String value = getString(key);
        if (value == null || value.isEmpty()) return def_value;

        value = value.substring(1, value.length() - 1);

        String[] list = value.split(", ");
        int[] list_int = new int[list.length];
        for (int i = 0; i < list.length; i++) {
            try {
                list_int[i] = Integer.parseInt(list[i]);
            } catch (NumberFormatException e) {
                System.err.println(list[i] + " is not an integer");
                return def_value;
            }
        }

        return list_int;
    }


    public static String[] getValue_fromData(String key, String[] data) {

        if (key.contains(".")) {
            String[] keys = key.split("\\.");
            String[] value = getValue_fromData(keys[0], data);
            if (value == null) {
                return null;
            }
            for (int i = 1; i < keys.length; i++) {
                value = getValue_fromData(keys[i], value);
                if (value == null) {
                    return null;
                }
            }
            System.out.println("funzt");
            return value;

        }


        int open = -1;
        int close = -1;
        int line_open = -1;
        int line_close = -1;

        for (int i = 0; i < data.length; i++) {
            String line = data[i].replace("\t", "");
            if (line.startsWith(key)) {
                if (line.startsWith(key + ": {")) {
                    line_open = i;
                    break;
                } else {
                    return new String[]{line.replaceFirst(key + ": ", "")};
                }
            }
        }


        if (line_open >= 0) {
            for (int i = line_open; i < data.length; i++) {
                String line = data[i];
                boolean ignore = false;
                if (!line.startsWith("//")) {
                    for (char c : line.toCharArray()) {
                        if (c == '"') ignore = !ignore;
                        if (!ignore) {
                            if (c == '{') {
                                open++;
                            } else if (c == '}') {
                                close++;
                            }
                        }
                        if (close != -1 && open == close) {
                            line_close = i + 1;

                            String last = data[line_open + (i - line_open)];
                            last = last.replace("}", "");
                            if (last.isEmpty())
                                line_close--;

                            String first = data[line_open];
                            first = first.replaceFirst(key + ": \\{", "");
                            if (first.isEmpty())
                                line_open++;

                            int diff = line_close - line_open;
                            String[] out = new String[diff];

                            for (int j = 0; j < diff; j++) {
                                if (j == diff - 1) {
                                    out[j] = data[line_open + j].replace("}", "").replaceFirst("\t", "");
                                } else {
                                    out[j] = data[line_open + j].replaceFirst("\t", "");
                                }
                            }
                            return out;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static String[] getValue_fromData(String key, ArrayList<String> value) {
        String[] out = new String[value.size()];
        for (int i = 0; i < value.size(); i++) {
            out[i] = value.get(i);
        }
        return getValue_fromData(key, out);
    }

    public static String[] getStrings_fromData(String key, ArrayList<String> value) {
        String[] out = new String[value.size()];
        for (int i = 0; i < value.size(); i++) {
            out[i] = value.get(i);
        }
        return getValue_fromData(key, out);
    }
}
