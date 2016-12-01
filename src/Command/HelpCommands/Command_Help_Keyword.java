/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.HelpCommands;

import Command.Command;
import ConstantData.Message_Data;
import Message.Message;
import ircbot.IRCBot;

public class Command_Help_Keyword implements Command {
    @Override public Command_Validity validate(Message msg) {
        if(msg.getText().equals(Message_Data.helpkeyword_command))
            return Command_Validity.CMD_VALID;
        return Command_Validity.CMD_BADFORMAT;
    }
    
    @Override public void operate(Message msg) {
        IRCBot Bot = IRCBot.getInstance();
        String toSend = Message_Data.keyword_help_message;
        Bot.sendMessage(msg.getChannel(), toSend);
    }
}
