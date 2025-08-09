package com.github.theprogmatheus.devroom.treasurehunt.command.subcmd;

import com.github.theprogmatheus.devroom.treasurehunt.command.AbstractCommand;

public class DeleteCommand extends AbstractCommand {

    public DeleteCommand() {
        super("delete", "Delete an treasure.", "delete <treasureId>");
    }

}
