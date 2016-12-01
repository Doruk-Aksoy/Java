package Command;

import java.sql.SQLException;
import java.sql.ResultSet;

import ConstantData.Message_Data;
import Database.*;
import Message.Message;
import Parsing.*;
import UserType.*;
import ircbot.IRCBot;
import ircbot.IRCBot_UserManager;

public class Command_Login implements Command {
    private String[] text; // has a string array associated with parsing to avoid splitting twice
    
    private boolean verifyFormat(String[] s) {
        if(s != null && s.length == 3)
            return true;
        return false;
    }
    
    @Override public Command_Validity validate(Message msg) {
        if(msg.getType() != Message.Message_Type.MSG_PM)
            return Command_Validity.CMD_POSSIBLYBAD;
        Parser p = new StringSeperator();
        text = p.parse(msg.getText());
        if(!verifyFormat(text))
            return Command_Validity.CMD_BADFORMAT;
        return Command_Validity.CMD_VALID;
    }
    
    @Override public void operate(Message msg) {
        IRCBot Bot = IRCBot.getInstance();
        String sender = msg.getSender();
        Database_Connection DB = Database_Connection.getInstance();
        Bot.sendMessage(msg, Message_Data.login_begin_message);
        try {
            Query_Handler QH = new Query_Handler();
            DB.connect();
            // 1 is the first parameter
            if(!QH.userExists(DB.getConnection(), text[1]))
                Bot.sendMessage(msg, Message_Data.login_nouser);
            else {
                // get the md5 hash of password
                Parser hasher = new MD5Hasher();
                String pass = hasher.parse(text[2])[0];
                if(QH.checkPassword(DB.getConnection(), text[1], pass)) {
                    IRCBot_UserManager UM = Bot.getUserManager();
                    // did this user log in already
                    if(UM.getUser(text[1]) != null)
                        Bot.sendMessage(msg, Message_Data.login_already);
                    else {
                        ResultSet rs = QH.getMostRecentResult();
                        // we know this has to be a unique result
                        UserCredentials uc = new UserCredentials(sender, text[1], msg.getLogin(), msg.getHostname());
                        GameUser user = new NormalUser(uc, rs.getInt("ID"), 0);
                        UM.addUser(user);
                        Bot.sendMessage(msg, Message_Data.login_success);
                    }
                }
                else
                    Bot.sendMessage(msg, Message_Data.login_invalid_password);
            }
        }
        catch (SQLException e) {
            Bot.sendMessage(sender, Message_Data.register_dbconnection_fail);
            System.out.println(e);
        }
        // close database after registering
        finally {
            try {
                DB.disconnect();
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
    }
}
