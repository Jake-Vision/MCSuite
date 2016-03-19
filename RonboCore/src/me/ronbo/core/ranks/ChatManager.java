package me.ronbo.core.ranks;

import java.util.HashMap;

import me.ronbo.core.RonboCore;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatManager implements Listener {
	
	private static final String[] CUSSWORDS = {"2g1c","2 girls 1 cup","acrotomophilia","anal","anilingus","arsehole", "asshole","assmunch","auto erotic","autoerotic","babeland","baby batter","ball gag","ball gravy","ball kicking","ball licking","ball sack","ball sucking","bangbros","bareback","barely legal","barenaked","bastard","bastinado","bbw","bdsm","beaver cleaver","beaver lips","bestiality","bi curious","big black","big breasts","big knockers","big tits","bimbos","birdlock","bitch","black cock","blonde action","blonde on blonde action","blow j","blow your l","blue waffle","blumpkin","bollocks","bondage","booty call","brown showers","brunette action","bukkake","bulldyke","bullet vibe","bung hole","bunghole","busty","buttcheeks","butthole","camel toe","camgirl","camslut","camwhore","carpet muncher","carpetmuncher","chocolate rosebuds","circlejerk","cleveland steamer","clit","clitoris","clover clamps","clusterfuck","cock","cocks","coprolagnia","coprophilia","cornhole","cum","cumming","cunnilingus","cunt","darkie","date rape","daterape","deep throat","deepthroat","dick","dildo","dirty pillows","dirty sanchez","doggie style","doggiestyle","doggy style","doggystyle","dog style","dolcett","domination","dominatrix","dommes","donkey punch","double dong","double penetration","dp action","eat my ass","ecchi","ejaculation","erotic","erotism","escort","ethical slut","eunuch","faggot","fecal","felch","fellatio","feltch","female squirting","femdom","figging","fingering","fisting","foot fetish","footjob","frotting","fuck","fuck buttons","fudge packer","fudgepacker","futanari","gang bang","gay sex","genitals","giant cock","girl on","girl on top","girls gone wild","goatcx","goatse","gokkun","golden shower","goodpoop","goo girl","goregasm","grope","group sex","g-spot","guro","hand job","handjob","homoerotic","honkey","hooker","hot chick","how to murder","huge fat","humping","incest","intercourse","jack off","jail bait","jailbait","jerk off","jigaboo","jiggaboo","jiggerboo","jizz","juggs","kike","kinbaku","kinkster","kinky","knobbing","leather restraint","leather straight jacket","lemon party","lolita","lovemaking","make me come","male squirting","masturbate","menage a trois","milf","missionary position","motherfucker","mound of venus","mr hands","muff diver","muffdiving","nambla","nawashi","negro","neonazi","nigga","nigger","nig nog","nimphomania","nipple","nipples","nsfw images","nude","nudity","nympho","nymphomania","octopussy","omorashi","one cup two girls","one guy one jar","orgasm","orgy","paedophile","panties","panty","pedobear","pedophile","pegging","penis","phone sex","piece of shit", "piss", "pissing","piss pig","pisspig","playboy","pleasure chest","pole smoker","ponyplay","poop chute","poopchute","porn","porno","pornography","prince albert piercing","pthc","pubes","pussy","queaf","raghead","raging boner","rape","raping","rapist","rectum","reverse cowgirl","rimjob","rimming","rosy palm","rosy palm and her 5 sisters","rusty trombone","sadism","schlong","scissoring","semen","sex","sexo","sexy","shaved beaver","shaved pussy","shemale","shibari","shit","shota","shrimping","slanteye","slut","s&m","smut","snatch","snowballing","sodomize","sodomy","spic","spooge","spread legs","strap on","strapon","strappado","strip club","style doggy","suicide girls","sultry women","swastika","swinger","tainted love","taste my","tea bagging","threesome","throating","tied up","tight white","tongue in a","topless","tosser","towelhead","tranny","tribadism","tub girl","tubgirl","tushy","twat","twink","twinkie","two girls one cup","undressing","upskirt","urethra play","urophilia","vagina","venus mound","vibrator","violet blue","violet wand","vorarephilia","voyeur","vulva","wank","wetback","wet dream","white power","women rapping","wrapping men","wrinkled starfish" ,"xxx","yellow showers","yiffy","zoophilia"};
	private static final String FILLER = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@";
	public static boolean override = false;
	
	public static String filter(String s) {
		String s3 = "";
		if(s.contains("{\"text\":")) {
			int index = s.indexOf("{\"text\":");
			index = s.indexOf(":", "{\"text\":".length());
			index = s.indexOf(":", index + 1);
			if(index >= 0) {
				s3 = s.substring(0, index);
				s = s.substring(index);
			}
		}
		String lowercase = s.toLowerCase();
		for(String s2 : CUSSWORDS) {
			if(lowercase.contains(s2))
				lowercase = lowercase.replaceAll(s2, FILLER.substring(0, s2.length()));
		}
		if(!lowercase.equals(s.toLowerCase()))
			s = lowercase;
		return s3 + s;
	}
	
	public static String rankFormat(Player p, String s) {
		int power = RankManager.getRankPower(p.getName());
		String format = ChatColor.GRAY + (RankManager.check(p, "vip") || RankManager.checkEqualsExact(p, "lord") ? ChatColor.getLastColors(RankManager.getPrefix(p.getName())) : power > RankManager.rankPowers.get("member") ? ChatColor.WHITE + "" : "") + s;
		return format;
	}
	
	public static String makeMessage(Player p, String message) { 
		String name = p.getName();
		String prefix = RankManager.getPrefix(name);
		String stripped = ChatColor.stripColor(prefix);
		if(prefix.length() > 0)
			prefix = ChatColor.getLastColors(prefix) + ChatColor.BOLD + stripped + " ";
		if(stripped.contains("Lord"))
			prefix = RankManager.getPrefix(name) + " ";
		return prefix + (name.equals("xRonbo") ? ChatColor.GOLD : ChatColor.WHITE) + name + ChatColor.WHITE + ": " + rankFormat(p, filter(message));
	}
	
	public static long chatCooldown = 0;
	public static HashMap<String, Long> lastchat = new HashMap<String, Long>();
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if(override)
			return;
		event.setCancelled(true);
		String s = makeMessage(event.getPlayer(), event.getMessage());
		int id = -1;
		if(event.getPlayer().hasMetadata("id")) {
			try {
				id = event.getPlayer().getMetadata("id").get(0).asInt();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		boolean canChat = false;
		if(RankManager.check(event.getPlayer(), "knight")) {
			canChat = true;
		} else {
			if(lastchat.containsKey(event.getPlayer().getName())) {
				if(System.currentTimeMillis() - lastchat.get(event.getPlayer().getName()) < chatCooldown) {
					canChat = false;
				} else {
					canChat = true;
				}
			} else {
				canChat = true;
			}
		}
		for(String filtered : RonboCore.plugin.filter) {
			if(s.toLowerCase().contains(filtered)) {
				event.getPlayer().sendMessage(ChatColor.RED + "Your message was removed by Kastia's automated chat filter.");
				return;
			}
		}
		if(RonboCore.plugin.nochat.contains(event.getPlayer().getName())) {
			event.getPlayer().sendMessage(ChatColor.RED + "You must move before chatting!");
			return;
		}
		if(RonboCore.plugin.globalmute) {
			if(!RankManager.check(event.getPlayer(), "helper")) {
				event.getPlayer().sendMessage(ChatColor.RED + "Kastia is currently in silent mode - only staff can chat.");
				return;
			}
		}
		if(canChat) {
			lastchat.put(event.getPlayer().getName(), System.currentTimeMillis());
			boolean spectate = event.getPlayer().hasMetadata("spectate");
			if(spectate)
				s = ChatColor.AQUA + "[Spectator Chat] " + ChatColor.RESET + s;
			for(Player p : RonboCore.plugin.getServer().getOnlinePlayers()) {
				if(spectate && !p.hasMetadata("spectate"))
					continue;
				try {
					if(p.hasMetadata("id") && id == p.getMetadata("id").get(0).asInt())
						p.sendMessage(s);
					else if(!p.hasMetadata("id") && id == -1)
						p.sendMessage(s);
				} catch(Exception e) {
					
				}
			}
		} else {
			String delay = String.format("%.1f", ((double)chatCooldown - (System.currentTimeMillis() - lastchat.get(event.getPlayer().getName())))/1000.0);
			event.getPlayer().sendMessage(ChatColor.RED + "You cannot chat for another " + delay + " seconds!");
		}
		System.out.println(s);
	}
	
	public ChatManager() {
		
	}
}
