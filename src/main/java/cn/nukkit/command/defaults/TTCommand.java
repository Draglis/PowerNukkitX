package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.tree.node.PlayersNode;
import cn.nukkit.command.utils.CommandLogger;
import cn.nukkit.inventory.fake.FakeStructBlock;
import cn.nukkit.level.Level;
import org.iq80.leveldb.util.FileUtils;

import java.io.File;
import java.util.Map;


public class TTCommand extends TestCommand {
    FakeStructBlock fakeStructBlock;

    public TTCommand(String name) {
        super(name, "tt");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newEnum("sub", new String[]{"get", "set"}),
                CommandParameter.newType("name", false, CommandParamType.TARGET, new PlayersNode()),
        });
        this.enableParamTree();
    }

    @Override
    public int execute(CommandSender sender, String commandLabel, Map.Entry<String, ParamList> result, CommandLogger log) {
        ParamList value = result.getValue();
        String s = value.getResult(0);

        if (sender.isOp()) {
            Player p = sender.asPlayer();
            switch (s) {
                case "get" -> {
                }
                case "set" -> {
                    Level level = p.getLevel();
                    String name = level.getFolderName();
                    if (level.equals(Server.getInstance().getDefaultLevel())) {
                        p.sendMessage("Probably shouldn't try on default world xd - Cancelled. try a different world.");
                        return 1;
                    } else {
                        String var10002 = Server.getInstance().getDataPath();
                        File one = new File(var10002 + "worlds/" + name);
                        File two = new File(Server.getInstance().getDataPath() + "preset");
                        if (!two.exists()) {
                            p.sendMessage("§cNo preset folder found. Copy world as §e" + Server.getInstance().getDataPath() + "preset");
                            return 1;
                        } else {
                            try {
                                level.unload(true);
                            } catch (Exception ignore) {
                            }

                            var10002 = Server.getInstance().getDataPath();
                            File regionfolder = new File(var10002 + "worlds/" + name + "/db");
                            var10002 = Server.getInstance().getDataPath();
                            File worldfolder = new File(var10002 + "worlds/" + name);
                            FileUtils.deleteDirectoryContents(regionfolder);
                            FileUtils.deleteDirectoryContents(worldfolder);
                            worldfolder.delete();
                            one.delete();
                            FileUtils.copyDirectoryContents(two, one);
                            Server.getInstance().loadLevel(name);
                            p.teleport(Server.getInstance().getLevelByName(name).getSafeSpawn());
                            return 1;
                        }
                    }
                }
            }
            return 1;
        } else return 0;
    }
}
