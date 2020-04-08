package ru.students.lab.commands;

import java.io.IOException;

/**
 * Класс для выполнения и получения информации о функции сохранения коллекции в файл
 * @autor Хосе Ортис
 * @version 1.0
*/

public class ExitCommand extends AbsCommand {

    public final String description = "завершить программу (без сохранения в файл))";

    @Override
    public Object execute(ExecutionContext context) throws IOException {
        return null;
    }

}
