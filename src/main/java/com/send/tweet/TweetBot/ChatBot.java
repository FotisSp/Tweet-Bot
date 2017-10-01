package com.send.tweet.TweetBot;

import java.util.ArrayList;

import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class ChatBot {
	private static final String oAuthConsumerKey = "XXXXXXXXXXXXXXXXXXXXXXXXX";
	private static final String oAuthConsumerSecret = "XXXXXXXXXXXXXXXXXXXXXXXXX";
	private static final String oAuthAccessToken = "XXXXXXXXXXXXXXXXXXXXXXXXX-XXXXXXXXXXXXXXXXXXXXXXXXX";
	private static final String oAuthAccessTokenSecret = "XXXXXXXXXXXXXXXXXXXXXXXXX";
	private int progress = 0;
	private int userNumber = 0;
	private long startTime = System.currentTimeMillis();
	private Twitter twitter;
	private ArrayList<String> topics = new ArrayList<String>();
	private String currentTopic = null;
	
	public ChatBot(Twitter twitter) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey(oAuthConsumerKey)
          .setOAuthConsumerSecret(oAuthConsumerSecret);
        
        this.twitter = twitter;
        UserStreamListener userStreamListener = communicationHandler();
	    
        TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(cb.build());
        TwitterStream twitterStream = twitterStreamFactory.getInstance(new AccessToken(oAuthAccessToken, oAuthAccessTokenSecret));
        twitterStream.addListener(userStreamListener);
        twitterStream.user();
	}

	private UserStreamListener communicationHandler() {
		UserStreamListener userStreamListener = new UserStreamListener() {

			public void onDeletionNotice(StatusDeletionNotice arg0) {
			}

			public void onScrubGeo(long arg0, long arg1) {
			}

			public void onStallWarning(StallWarning arg0) {
			}

			public void onStatus(Status arg0) {
			}

			public void onTrackLimitationNotice(int arg0) {
			}

			public void onException(Exception arg0) {
			}

			public void onBlock(User arg0, User arg1) {
			}

			public void onDeletionNotice(long arg0, long arg1) {
			}

			public void onDirectMessage(DirectMessage message) {
				String sender = message.getSenderScreenName();
				System.out.println("Sender : "+sender+" : receipment "+message.getRecipientScreenName());

				if(sender != "ThesisBotUOI")
				{
					try {
						if(message.getText().equals("!progress")) {
//							twitter.sendDirectMessage(sender,"Currently i am at : "+progress+" tweets for "+currentTopic+".");
							twitter.sendDirectMessage(sender,"Currently i am at : "+progress+" / "+userNumber+".");

						} else if(message.getText().equals("!uptime")) {
							long endTime = System.currentTimeMillis() - startTime;
							long sec = endTime / 1000 % 60;
							long min = endTime / (60 * 1000) % 60;
							long hr= endTime / (60 * 60 * 1000) % 24;
							long d = endTime / (60 * 60 * 1000 * 24);
							twitter.sendDirectMessage(sender,"Uptime : "+d+":"+hr+":"+min+":"+sec);
						} else if(message.getText().equals("!topic") && currentTopic != null) {
							twitter.sendDirectMessage(sender, "Currently working on topic : "+currentTopic);
						} else if(message.getText().equals("!remaining") && !topics.isEmpty()) {
							twitter.sendDirectMessage(sender, "Currently working on : "+currentTopic+" and "+printTopics()+" remain to be retrieved.");
						} else if(message.getText().equals("!remaining") && topics.isEmpty()) {
							twitter.sendDirectMessage(sender, "Currently working on : "+currentTopic+" and there are no more topics to search.");
						} else if(message.getText().equals("!help")) {
							twitter.sendDirectMessage(sender,"Available commands are :\n!progress\n!uptime\n!topic\n!remaining");
						} else if(message.getText().equals("!kill")) {
							System.exit(1);
						} else {
							twitter.sendDirectMessage(sender,"Type !help to see available commands.");
						}
					} catch (TwitterException e) {
						System.out.println("DM ERROR :\n"+e.getMessage()+"\nAT USER "+sender);
					}
				}
			}

			public void onFavorite(User arg0, User arg1, Status arg2) {
			}

			public void onFavoritedRetweet(User arg0, User arg1, Status arg2) {
			}

			public void onFollow(User arg0, User arg1) {
			}

			public void onFriendList(long[] arg0) {
			}

			public void onQuotedTweet(User arg0, User arg1, Status arg2) {
			}

			public void onRetweetedRetweet(User arg0, User arg1, Status arg2) {
			}

			public void onUnblock(User arg0, User arg1) {
			}

			public void onUnfavorite(User arg0, User arg1, Status arg2) {
			}

			public void onUnfollow(User arg0, User arg1) {
			}

			public void onUserDeletion(long arg0) {
			}

			public void onUserListCreation(User arg0, UserList arg1) {
			}

			public void onUserListDeletion(User arg0, UserList arg1) {
			}

			public void onUserListMemberAddition(User arg0, User arg1, UserList arg2) {
			}

			public void onUserListMemberDeletion(User arg0, User arg1, UserList arg2) {
			}

			public void onUserListSubscription(User arg0, User arg1, UserList arg2) {
			}

			public void onUserListUnsubscription(User arg0, User arg1, UserList arg2) {
			}

			public void onUserListUpdate(User arg0, UserList arg1) {
			}

			public void onUserProfileUpdate(User arg0) {
			}

			public void onUserSuspension(long arg0) {
			}
	    };
		return userStreamListener;
	}
	
	public void setProgress(int progress) {
		this.progress = progress;
	}
	
	public void setUserNumber(int userNumber) {
		this.userNumber = userNumber;
	}
	
	public void setWorkingTopics(ArrayList<String> topics) {
		this.topics = topics;
	}
	
	public boolean removeTopic(String topic) {
		if(topics.remove(topic)) {
			currentTopic  = topic;
			return true;
		}
		return false;
	}
	
	private String printTopics() {
		String builder = "";
		for(String topic : topics)
		{
			builder += topic+", ";
		}
		return builder;
	}
}
