package com.github.theprogmatheus.devroom.treasurehunt.command.subcmd;

import com.github.theprogmatheus.devroom.treasurehunt.command.AbstractCommand;

public class CompletedCommand extends AbstractCommand {


    public CompletedCommand() {
        super("completed", "Returns the list of players who found that treasure", "completed <treasureId>");
    }
}
