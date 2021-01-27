import cacher.Cacher;
import exception.LangParseException;
import function_table.Function;
import function_table.FunctionTable;
import stack_machine.ReversePolishNotationInterpreter;
import stack_machine.ReversePolishNotationTranslator;
import token.Token;
import lexer.Lexer;
import parser.Parser;
import triad_optimizer.TriadOptimizer;
import type_table.Method;
import type_table.TypeTable;
import types.hash.CatHashTable;
import types.lists.CatDoublyLinkedList;
import vartable.VarTable;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CatUI {
    private static class Arguments {
        public boolean noCache;
        public String filename;

        public Arguments() {
            noCache = false;
            filename = "";
        }
    }

    private static List<Token> mainRpn;

    public static void main(String[] args) throws Exception, LangParseException {
        Arguments arguments = new Arguments();

        if (args.length < 1) {
            System.exit(-1);
        }
        arguments.filename = args[0];
        if (args[0].equals("--no-cache")) {
            arguments.noCache = true;
            arguments.filename = args[1];
        }

        String rawInput = Files.readString(Paths.get(arguments.filename));

        Lexer lexer = new Lexer(rawInput);
        List<Token> lexerResult = lexer.getTokens();
        System.out.println("LEXER RESULT : " + lexerResult + "\n");

        Parser parser = new Parser(lexer.getTokens());
        //parser.lang();

        int programHash = lexerResult.hashCode();
        System.out.println("PROGRAM HASH : " + programHash + "\n");
        Cacher cacher = new Cacher(programHash, arguments.filename);

        if (!arguments.noCache && cacher.findCache()) {
            mainRpn = cacher.getRpn();
            cacher.configureVarTable();
            cacher.configureFunctionTable();
        }
        else {
            ReversePolishNotationTranslator translator = new ReversePolishNotationTranslator(lexer.getTokens());
            mainRpn = translator.getRpn();
            System.out.println("REVERSE POLISH NOTATION RESULT : " + mainRpn + "\n");
            System.out.println("TABLE OF VARIABLES : " + VarTable.getInstance() + "\n");
            System.out.println("TABLE OF FUNCTIONS : " + FunctionTable.getInstance() + "\n");

            optimizeCode();

            cacher.writeCache(programHash, mainRpn);
        }
        System.out.println("OPTIMIZED REVERSE POLISH NOTATION: " + mainRpn + "\n");
        System.out.println("UPDATED TABLE OF VARIABLES: " + VarTable.getInstance() + "\n");
        System.out.println("UPDATED TABLE OF FUNCTIONS: " + FunctionTable.getInstance() + "\n");

        configureTypeTable();
        ReversePolishNotationInterpreter rpnInterpreter = new ReversePolishNotationInterpreter(mainRpn);

        System.out.println("<--------+ PROGRAM RESULT +-------->");
        rpnInterpreter.interpret();
    }

    private static void optimizeCode() {
        // Optimizing main code
        System.out.println("Optimizing main code:");
        TriadOptimizer optimizer = new TriadOptimizer(mainRpn);
        optimizer.optimize();

        Map<String, VarTable.VarData> varTableDataCopy = new HashMap<>();
        varTableDataCopy.putAll(VarTable.getInstance().getData());
        // Optimizing functions code
        for (Map.Entry<String, Function> entry : FunctionTable.getInstance().entrySet()) {
            System.out.println("Optimizing " + entry.getKey() + " function:");
            VarTable.getInstance().setData(entry.getValue().getVarTableData());
            TriadOptimizer functionOptimizer = new TriadOptimizer(entry.getValue().getBody());
            functionOptimizer.optimize();
        }
        VarTable.getInstance().setData(varTableDataCopy);

        clearVarTable();
    }

    private static void configureTypeTable() {
        initList();
        initMap();
    }

    private static void initList() {
        TypeTable.getInstance().put("list", new ArrayList<>(Arrays.asList(
                new Method(".add", new ArrayList<>(Arrays.asList("Object")), "", (arg0, arg1) -> {
                    CatDoublyLinkedList list = (CatDoublyLinkedList) arg0;
                    ArrayList<Object> argsList = (ArrayList<Object>) arg1;
                    list.add(argsList.get(0));

                    return null;
                }),
                new Method(".insert", new ArrayList<>(Arrays.asList("int", "Object")), "", (arg0, arg1) -> {
                    CatDoublyLinkedList list = (CatDoublyLinkedList) arg0;
                    ArrayList<Object> argsList = (ArrayList<Object>) arg1;
                    list.insert((int) argsList.get(0), argsList.get(1));

                    return null;
                }),
                new Method(".get", new ArrayList<>(Arrays.asList("int")), "Object", (arg0, arg1) -> {
                    CatDoublyLinkedList list = (CatDoublyLinkedList) arg0;
                    ArrayList<Object> argsList = (ArrayList<Object>) arg1;
                    return list.get((int) argsList.get(0));
                }),
                new Method(".remove", new ArrayList<>(Arrays.asList("int")), "", (arg0, arg1) -> {
                    CatDoublyLinkedList list = (CatDoublyLinkedList) arg0;
                    ArrayList<Object> argsList = (ArrayList<Object>) arg1;
                    list.removeNodeByIndex((int) argsList.get(0));

                    return null;
                }),
                new Method(".size", new ArrayList<>(), "int", (arg0, arg1) -> {
                    CatDoublyLinkedList list = (CatDoublyLinkedList) arg0;
                    return list.size();
                }),
                new Method(".isEmpty", new ArrayList<>(), "int", (arg0, arg1) -> {
                    CatDoublyLinkedList list = (CatDoublyLinkedList) arg0;
                    return list.isEmpty() ? 1 : 0;
                }),
                new Method(".clear", new ArrayList<>(), "", (arg0, arg1) -> {
                    CatDoublyLinkedList list = (CatDoublyLinkedList) arg0;
                    list.clear();

                    return null;
                })
        )));
    }

    private static void initMap() {
        TypeTable.getInstance().put("hashtable", new ArrayList<>(Arrays.asList(
                new Method(".put", new ArrayList<>(Arrays.asList("Object", "Object")), "", (arg0, arg1) -> {
                    CatHashTable hashMap = (CatHashTable) arg0;
                    ArrayList<Object> argsList = (ArrayList<Object>) arg1;
                    hashMap.put(argsList.get(0), argsList.get(1));

                    return null;
                }),
                new Method(".get", new ArrayList<>(Arrays.asList("Object")), "Object", (arg0, arg1) -> {
                    CatHashTable hashMap = (CatHashTable) arg0;
                    ArrayList<Object> argsList = (ArrayList<Object>) arg1;
                    return hashMap.get(argsList.get(0));
                }),
                new Method(".remove", new ArrayList<>(Arrays.asList("Object")), "", (arg0, arg1) -> {
                    CatHashTable hashMap = (CatHashTable) arg0;
                    ArrayList<Object> argsList = (ArrayList<Object>) arg1;
                    hashMap.remove(argsList.get(0));

                    return null;
                }),
                new Method(".size", new ArrayList<>(), "int", (arg0, arg1) -> {
                    CatHashTable hashMap = (CatHashTable) arg0;
                    return hashMap.size();
                }),
                new Method(".clear", new ArrayList<>(), "", (arg0, arg1) -> {
                    CatHashTable hashMap = (CatHashTable) arg0;
                    hashMap.clear();

                    return null;
                })
        )));

    }

    private static void clearVarTable() {
        Iterator<String> it = VarTable.getInstance().keySet().iterator();
        while (it.hasNext()) {
            if (it.next().charAt(0) != '_') {
                it.remove();
            }
        }
    }
}
