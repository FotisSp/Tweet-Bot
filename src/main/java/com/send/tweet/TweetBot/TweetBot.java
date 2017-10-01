package com.send.tweet.TweetBot;

import java.util.ArrayList;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TweetBot {
	private static final String oAuthConsumerKey = "XXXXXXXXXXXXXXXXXXXXXXXXX";
	private static final String oAuthConsumerSecret = "XXXXXXXXXXXXXXXXXXXXXXXXX";
	private static final String oAuthAccessToken = "XXXXXXXXXXXXXXXXXXXXXXXXX-XXXXXXXXXXXXXXXXXXXXXXXXX";
	private static final String oAuthAccessTokenSecret = "XXXXXXXXXXXXXXXXXXXXXXXXX";
	private static Twitter twitter;
	private ChatBot chatBot;
	
	public TweetBot() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(oAuthConsumerKey)
		.setOAuthConsumerSecret(oAuthConsumerSecret)
		.setOAuthAccessToken(oAuthAccessToken)
		.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
		twitter = new TwitterFactory(cb.build()).getInstance();
		chatBot = new ChatBot(twitter);
	}
	
	/**
	 * Creates a tweet and publishes it on twitter
	 * @param tweet : String with message to be tweeted. Must not be longer than 140 characters
	 * 
	 * @return true if tweet was successful, false otherwise  
	 */
	public boolean sendTweet(String tweet)
	{
		if(tweet.length() <= 140) {
			try {
				twitter.updateStatus(tweet);
				return true;
			} catch (TwitterException e) {
				System.out.println("TWEET ERROR : "+e.getMessage());
			}
		}
		return false;
	}
	
	/**
	 * Send Direct Message to a single user
	 * @param message String text to be sent
	 * @param userName String UserName to send message
	 * @return true if DM was successful, false otherwise
	 */
	public boolean sendDM(String message,String userName)
	{
		try {
			twitter.sendDirectMessage(userName, message);
			return true;
		} catch (TwitterException e) {
			System.out.println("DM ERROR : "+e.getMessage());
		}
		return false;
	}
	
	/**
	 * Sends Direct Message to a list of users
	 * @param message String text message to be sent
	 * @param userNames ArrayList<String> with the names of the users
	 * @return true if all messages were delievered or false if one of them failed
	 */
	public boolean sendDM(String message,ArrayList<String> userNames)
	{
		for(String user : userNames)
		{
			try {
				twitter.sendDirectMessage(user, message);
			} catch (TwitterException e) {
				System.out.println("DM ERROR\n"+e.getMessage()+"\nAT USER "+user);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This method is needed for twitter chat bot to be used. It sets the current progress of the main program.
	 * @param progress Integer number of tweets that have been retrieved.
	 */
	public void setProgress(int progress) {
		chatBot.setProgress(progress);
	}
	
	public void setUserNumber(int userNumber) {
		chatBot.setUserNumber(userNumber);
	}
	
	/**
	 * Sets the ArrayList of topics for chat bot.
	 * @param topics ArrayList<String> with topics.
	 */
	public void setWorkingTopics(ArrayList<String> topics) {
		chatBot.setWorkingTopics(topics);
	}
	
	/**
	 * Sets the current working topic and then removes it from the list of remaining topics.
	 * @param topic to be set as current and be removed.
	 * @return true if remove was successful, false otherwise.
	 */
	public boolean removeTopic(String topic) {
		return chatBot.removeTopic(topic);
	}
}
