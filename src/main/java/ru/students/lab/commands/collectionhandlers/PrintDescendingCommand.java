package ru.students.lab.commands.collectionhandlers;

import ru.students.lab.commands.AbsCommand;
import ru.students.lab.commands.ExecutionContext;
import ru.students.lab.util.ListEntrySerializable;

import java.io.IOException;
import java.util.List;

/**
 * Класс для выполнения и получения информации о функции вывода элементов коллекции в порядке убывания
 * @autor Хосе Ортис
 * @version 1.0
*/
public class PrintDescendingCommand extends AbsCommand {

    public final String description = "вывести элементы коллекции в порядке убывания.\nSyntax: print_descending -{k/i/n/d} где: -k=key / -i=id / -n=name / -d=creation_date";

     @Override
     public Object execute(ExecutionContext context) throws IOException {
         context.result().setLength(0);
         List<ListEntrySerializable> sortedDragons = null;

         switch (args[0]) {
             case "":
             case "-k":
                 //System.out.println("Sorting by key...");
                 sortedDragons = context.collectionManager().sortByKey();
                 break;
             case "-i":
                 //System.out.println("Sorting by ID...");
                 sortedDragons = context.collectionManager().sortById();
                 break;
             case "-n":
                 //System.out.println("Sorting by Name...");
                 sortedDragons = context.collectionManager().sortByName();
                 break;
             case "-d":
                 //System.out.println("Sorting by Creation Date...");
                 sortedDragons = context.collectionManager().sortByCreationDate();
                 break;
             default:
                 context.result().append("This option is not available. Correct= -{k/i/n/d}");
         }
         if (sortedDragons != null)
             return sortedDragons;
         return context.result().toString();
     }

     @Override
     public String getDescription() {
         return description;
     }
 }
