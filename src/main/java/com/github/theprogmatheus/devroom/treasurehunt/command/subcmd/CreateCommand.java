package com.github.theprogmatheus.devroom.treasurehunt.command.subcmd;

import com.github.theprogmatheus.devroom.treasurehunt.command.AbstractCommand;

public class CreateCommand extends AbstractCommand {

    public CreateCommand() {
        super("create", "Create a new treasure.", "create <treasureId> <command>");
    }
}
