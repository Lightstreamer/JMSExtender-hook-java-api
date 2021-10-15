/*
 *  Copyright (c) Lightstreamer Srl
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.lightstreamer.jms_extender.hooks;

import java.io.File;
import java.util.Map;

/**
 * Abstract class that defines a JMS Extender hook. By extending this class
 * it is possible to receive notification for a number of operations
 * requested by the users, including: <ul>
 * <li>connection openings,
 * <li>session openings,
 * <li>consumers and producers creation,
 * <li>shared and/or durable subscriptions creation
 * <li>dedicated broker connection requests.
 * </ul>
 */
public abstract class JmsExtenderHook {

	
	///////////////////////////////////////////////////////////////////////////
	// Hook initialization
	
	/**
	 * Hook initialization. Called during the JMS Extender initialization, 
	 * it is passed the same directory where the JMS Connectors configuration
	 * is located.
	 * 
	 * @param configDir the directory where the JMS Connectors configuration is located.
	 * 
	 * @throws HookException in case the initialization can't complete successfully.
	 * In this case the JMS Extender will abort and the Lightstreamer sever will fail
	 * the initialization too.
	 */
	public void init(File configDir) throws HookException { /* Implementation intentionally left blank */ }

	
	///////////////////////////////////////////////////////////////////////////
	// Methods for user connection and authentication
	
	/**
	 * User connection and authentication request. Called when a user requests the
	 * creation of a connection to the JMS Extender. The connection being authenticated 
	 * is the Lightstreamer connection between the JMS JS Client and the JMS Extender, 
	 * and NOT a JMS connection between the JMS Extender and the JMS broker (for 
	 * which parameters are provided on the JMS Connectors configuration file,
	 * see docs). Full client context is passed, together with the client 
	 * principal when a client certificate is specified. Note that Lightstreamer
	 * server must be configured appropriately to receive the client principal
	 * (see <code>use_client_auth</code> and <code>force_client_auth</code> parameters). 
	 * Default implementation simply returns <code>true</code>.
	 * 
	 * @param connectionId the unique identifier of the user's connection.
	 * @param user the name of the user trying to connect.
	 * @param password the password of the user trying to connect.
	 * @param clientContext a key-value map containing properties of the user's 
	 * request; available keys are:<ul>
	 * <li><code>"REMOTE_IP"</code> - a string representation of the remote IP related 
	 * to the current connection; it may be a proxy address.
	 * <li><code>"REMOTE_PORT"</code> - string representation of the remote port 
	 * related to the current connection.
	 * <li><code>"USER_AGENT"</code> - the user-agent as declared in the current 
	 * connection HTTP header.
	 * <li><code>"FORWARDING_INFO"</code> - the comma-separated list of addresses 
	 * forwarded by intermediaries, obtained from the X-Forwarded-For HTTP header, 
	 * related to the current connection; intermediate proxies usually set this 
	 * header to supply connection routing information. Note that if the number of 
	 * forwards to be considered local to the Server environment has been specified 
	 * through the <code>skip_local_forwards</code> configuration element, in order 
	 * to better determine the remote address, then these forwards will not be 
	 * included in the list.
	 * <li><code>"LOCAL_SERVER"</code> - the name of the specific server socket 
	 * that handles the current connection, as configured through the 
	 * <code>http_server</code> or <code>https_server</code> element.
	 * <li><code>"HTTP_HEADERS"</code> - a map object that contains a name-value 
	 * pair for each header found in the HTTP request that originated the call.
	 * </ul>
	 * @param clientPrincipal the identification name reported in the client 
	 * TLS/SSL certificate supplied on the socket connection used to issue the 
	 * request that originated the call; it can be null if client has not 
	 * authenticated itself or the authentication has failed.
	 * 
	 * @return true if the user is cleared and the connection can proceed,
	 * false if the user must be denied the connection.
	 * @throws HookException in case the authentication can't complete successfully, 
	 * (the user will be denied the connection).
	 */
	@SuppressWarnings("rawtypes") 
	public boolean onConnectionRequest(String connectionId, String user, String password, Map clientContext, String clientPrincipal) throws HookException {
		return true;
	}
	
	/**
	 * User connection close notification. Called when a connection to the 
	 * JMS Extender has been closed. Default implementation does nothing.
	 * 
	 * @param connectionId the unique identifier of the user's connection.
	 */
	public void onConnectionClose(String connectionId) { /* Implementation intentionally left blank */ }

	
	///////////////////////////////////////////////////////////////////////////
	// Methods for dedicated connection authorization

	/**
	 * Dedicated JMS broker connection request. Called when a user requests a 
	 * dedicated connection to the JMS broker by specifying a client ID. Gives 
	 * an opportunity to refuse the dedicated connection. see "Use of shared and/or durable 
	 * subscriptions and scalability constraints" in the docs for more
	 * information. Default implementation simply returns <code>true</code>.
	 * 
	 * @param connectionId the unique identifier of the user's connection.
	 * @param jmsConnector the name of the JMS Connector the user is trying
	 * to open a dedicated JMS broker connection on.
	 * @param clientId the client ID specified by the user for the JMS broker connection
	 * (already mangled and/or decorated by {@link #getDedicatedBrokerConnectionName}).
	 * 
	 * @return true if the dedicated JMS broker connection can proceed,
	 * false if the dedicated connection must be denied.
	 * @throws HookException in case of a specific error 
	 * (the connection will be denied and the exception forwarded to the client).
	 */
	public boolean onDedicatedBrokerConnectionRequest(String connectionId, String jmsConnector, String clientId) throws HookException {
		return true;
	}

	
	///////////////////////////////////////////////////////////////////////////
	// Methods for session auditing

	/**
	 * Session open notification. Called when a user creates a new JMS session.
	 * Default implementation does nothing.
	 * 
	 * @param connectionId the unique identifier of the user's connection.
	 * @param jmsConnector the name of the JMS Connector the user opened the
	 * session on.
     * @param sessionGuid the GUID of the user's JMS session.
	 */
	public void onSessionOpen(String connectionId, String jmsConnector, String sessionGuid) { /* Implementation intentionally left blank */ }

	/**
	 * Session close notification. Called when an existing JMS session has been closed. 
	 * Note that sessions are closed on internal JMS Extender events, not on user request.
	 * Default implementation does nothing.
	 * 
	 * @param connectionId the unique identifier of the user's connection.
	 * @param jmsConnector the name of the JMS Connector the session was opened on.
     * @param sessionGuid the GUID of the user's JMS session.
	 */
	public void onSessionClose(String connectionId, String jmsConnector, String sessionGuid) { /* Implementation intentionally left blank */ }

	
	///////////////////////////////////////////////////////////////////////////
	// Methods for producer and consumer request and auditing
	
	/**
	 * Message consumer creation request. Called when a user requests the creation
	 * of a message consumer. Gives an opportunity to refuse the creation. 
	 * Note that shared and durable subscriptions are requested through dedicated methods. 
	 * Default implementation simply returns <code>true</code>.
	 * 
	 * @param connectionId the unique identifier of the user's connection.
	 * @param jmsConnector the name of the JMS Connector the user is trying
	 * to create the consumer on.
     * @param sessionGuid the GUID of the user's JMS session.
	 * @param destinationName the name of the destination addressed by the consumer.
	 * @param destinationIsTopic true if the destination is a topic.
	 * 
	 * @return true if the consumer creation can proceed,
	 * false if the consumer creation must be denied.
	 * @throws HookException in case of a specific error
	 * (the creation will be denied and the exception forwarded to the client).
	 */
	public boolean onMessageConsumerRequest(String connectionId, String jmsConnector, String sessionGuid, String destinationName, boolean destinationIsTopic) throws HookException {
		return true;
	}

	/**
	 * Message consumer close notification. Called when a message consumer has been
	 * closed. Default implementation does nothing.
	 * 
	 * @param connectionId the unique identifier of the user's connection.
	 * @param jmsConnector the name of the JMS Connector the consumer was opened on.
     * @param sessionGuid the GUID of the user's JMS session.
	 * @param destinationName the name of the destination addressed by the consumer.
	 * @param destinationIsTopic true if the destination is a topic.
	 */
	public void onMessageConsumerClose(String connectionId, String jmsConnector, String sessionGuid, String destinationName, boolean destinationIsTopic) { /* Implementation intentionally left blank */ }
	
	/**
	 * Durable subscription request. Called when a user requests a durable
	 * subscription. Gives an opportunity to refuse the subscription. 
	 * Default implementation simply returns <code>true</code>.
	 * 
	 * @param connectionId the unique identifier of the user's connection.
	 * @param jmsConnector the name of the JMS Connector the user is trying
	 * to open the durable subscription on.
	 * @param clientId the client ID specified by the user for the JMS connection.
     * @param sessionGuid the GUID of the user's JMS session.
	 * @param subscriptionName the name of the durable subscription 
	 * (already mangled and/or decorated by {@link #getDurableSubscriptionName}).
	 * @param topicName the name of the topic addressed by the durable subscription.
	 * 
	 * @return true if the durable subscription can proceed,
	 * false if the durable subscription must be denied.
	 * @throws HookException in case of a specific error
	 * (the subscription will be denied and the exception forwarded to the client).
	 */
	public boolean onDurableSubscriptionRequest(String connectionId, String jmsConnector, String clientId, String sessionGuid, String subscriptionName, String topicName) throws HookException {
		return true;
	}
	
    /**
     * Shared subscription request. Called when a user requests a shared
     * subscription. Gives an opportunity to refuse the subscription. 
     * Default implementation simply returns <code>true</code>.
     * 
     * @param connectionId the unique identifier of the user's connection.
     * @param jmsConnector the name of the JMS Connector the user is trying
     * to open the shared subscription on.
     * @param clientId the client ID specified by the user for the JMS connection.
     * @param sessionGuid the GUID of the user's JMS session.
     * @param subscriptionName the name of the shared subscription 
     * (already mangled and/or decorated by {@link #getSharedSubscriptionName}).
     * @param topicName the name of the topic addressed by the shared subscription.
     * 
     * @return true if the shared subscription can proceed,
     * false if the shared subscription must be denied.
     * @throws HookException in case of a specific error
     * (the subscription will be denied and the exception forwarded to the client).
     */
    public boolean onSharedSubscriptionRequest(String connectionId, String jmsConnector, String clientId, String sessionGuid, String subscriptionName, String topicName) throws HookException {
        return true;
    }
    
    /**
     * Shared durable subscription request. Called when a user requests a shared durable
     * subscription. Gives an opportunity to refuse the subscription. 
     * Default implementation simply returns <code>true</code>.
     * 
     * @param connectionId the unique identifier of the user's connection.
     * @param jmsConnector the name of the JMS Connector the user is trying
     * to open the shared durable subscription on.
     * @param clientId the client ID specified by the user for the JMS connection.
     * @param sessionGuid the GUID of the user's JMS session.
     * @param subscriptionName the name of the shared durable subscription 
     * (already mangled and/or decorated by {@link #getSharedDurableSubscriptionName}).
     * @param topicName the name of the topic addressed by the shared durable subscription.
     * 
     * @return true if the shared durable subscription can proceed,
     * false if the shared durable subscription must be denied.
     * @throws HookException in case of a specific error
     * (the subscription will be denied and the exception forwarded to the client).
     */
    public boolean onSharedDurableSubscriptionRequest(String connectionId, String jmsConnector, String clientId, String sessionGuid, String subscriptionName, String topicName) throws HookException {
        return true;
    }

	/**
	 * Durable subscription consumer close notification. Called when the consumer of a 
	 * durable subscription has been closed. Note that this does not include the unsubscription
	 * of durable subscription. Default implementation does nothing.
	 * 
	 * @param connectionId the unique identifier of the user's connection.
	 * @param jmsConnector the name of the JMS Connector the user opened the durable 
	 * subscription on.
	 * @param clientId the client ID specified by the user for the JMS connection.
     * @param sessionGuid the GUID of the user's JMS session.
	 * @param subscriptionName the name of the durable subscription.
	 * @param topicName the name of the topic addressed by the durable subscription.
	 */
	public void onDurableSubscriptionClose(String connectionId, String jmsConnector, String clientId, String sessionGuid, String subscriptionName, String topicName) { /* Implementation intentionally left blank */ }

    /**
     * Shared subscription consumer close notification. Called when the consumer of a 
     * shared subscription has been closed. Note that this does not include the unsubscription
     * of shared subscription. Default implementation does nothing.
     * 
     * @param connectionId the unique identifier of the user's connection.
     * @param jmsConnector the name of the JMS Connector the user opened the shared 
     * subscription on.
     * @param clientId the client ID specified by the user for the JMS connection.
     * @param sessionGuid the GUID of the user's JMS session.
     * @param subscriptionName the name of the shared subscription.
     * @param topicName the name of the topic addressed by the shared subscription.
     */
    public void onSharedSubscriptionClose(String connectionId, String jmsConnector, String clientId, String sessionGuid, String subscriptionName, String topicName) { /* Implementation intentionally left blank */ }

    /**
     * Shared durable subscription consumer close notification. Called when the consumer of a 
     * shared durable subscription has been closed. Note that this does not include the unsubscription
     * of shared durable subscription. Default implementation does nothing.
     * 
     * @param connectionId the unique identifier of the user's connection.
     * @param jmsConnector the name of the JMS Connector the user opened the shared durable 
     * subscription on.
     * @param clientId the client ID specified by the user for the JMS connection.
     * @param sessionGuid the GUID of the user's JMS session.
     * @param subscriptionName the name of the shared durable subscription.
     * @param topicName the name of the topic addressed by the shared durable subscription.
     */
    public void onSharedDurableSubscriptionClose(String connectionId, String jmsConnector, String clientId, String sessionGuid, String subscriptionName, String topicName) { /* Implementation intentionally left blank */ }

	/**
	 * Message producer creation request. Called when a user requests the creation
	 * of a message producer. Gives an opportunity to refuse the creation.
	 * Default implementation simply returns <code>true</code>.
	 * 
	 * @param connectionId the unique identifier of the user's connection.
	 * @param jmsConnector the name of the JMS Connector the user is trying
	 * to create the producer on.
     * @param sessionGuid the GUID of the user's JMS session.
	 * @param destinationName the name of the destination addressed by the producer.
	 * @param destinationIsTopic true if the destination is a topic.
	 * 
	 * @return true if the producer creation can proceed,
	 * false if the producer creation must be denied.
	 * @throws HookException in case of a specific error
	 * (the creation will be denied and the exception forwarded to the client).
	 */
	public boolean onMessageProducerRequest(String connectionId, String jmsConnector, String sessionGuid, String destinationName, boolean destinationIsTopic) throws HookException {
		return true;
	}

	/**
	 * Message producer close notification. Called when a message producer has been
	 * closed. Note that producers are closed on internal JMS Extender events, not on 
	 * user request. Default implementation does nothing.
	 * 
	 * @param connectionId the unique identifier of the user's connection.
	 * @param jmsConnector the name of the JMS Connector the producer was opened on.
     * @param sessionGuid the GUID of the user's JMS session.
	 * @param destinationName the name of the destination addressed by the producer.
	 * @param destinationIsTopic true if the destination is a topic.
	 */
	public void onMessageProducerClose(String connectionId, String jmsConnector, String sessionGuid, String destinationName, boolean destinationIsTopic) { /* Implementation intentionally left blank */ }

	
    ///////////////////////////////////////////////////////////////////////////
    // Methods for object message serialization
	
	/**
	 * Object message payload class request. Called when a user requests to send
	 * an object message, before the payload class is initialized. Gives an opportunity 
	 * to refuse sending the message or altering the payload class.
	 * Default implementation simply returns the value of <code>classFullyQualifiedName</code>.
	 * 
	 * @param connectionId the unique identifier of the user's connection.
	 * @param jmsConnector the name of the JMS Connector the user is trying
     * @param sessionGuid the GUID of the user's JMS session.
     * @param destinationName the name of the destination addressed by the producer.
     * @param destinationIsTopic true if the destination is a topic.
	 * @param classFullyQualifiedName requested fully qualified name of the class for the object message payload.
	 * 
	 * @return the name of the class accepted for the object message payload
	 * (may be different than the requested one).
	 * @throws HookException in case of a specific error
     * (the request will be denied and the exception forwarded to the client).
	 */
	public String onObjectMessagePayloadClassRequest(String connectionId, String jmsConnector, String sessionGuid, String destinationName, boolean destinationIsTopic, String classFullyQualifiedName) throws HookException { 
	    return classFullyQualifiedName;
	}
	
    /**
     * Object message payload request. Called when a user requests to send
     * an object message, after the payload class is initialized, instantiated and filled with the
     * user content. Gives an opportunity to refuse sending the message or altering the payload.
     * Default implementation simply returns the value of <code>payload</code>.
     * 
     * @param connectionId the unique identifier of the user's connection.
     * @param jmsConnector the name of the JMS Connector the user is trying
     * @param sessionGuid the GUID of the user's JMS session.
     * @param destinationName the name of the destination addressed by the producer.
     * @param destinationIsTopic true if the destination is a topic.
     * @param payload requested object message payload.
     * 
     * @return the accepted payload for the object message
     * (may be different than the requested one).
     * @throws HookException in case of a specific error
     * (the request will be denied and the exception forwarded to the client).
     */
    public Object onObjectMessagePayloadRequest(String connectionId, String jmsConnector, String sessionGuid, String destinationName, boolean destinationIsTopic, Object payload) throws HookException { 
        return payload;
    }

	
	///////////////////////////////////////////////////////////////////////////
	// Methods for client ID and subscription name decoration

	/**
	 * Dedicated JMS broker connection name mangling or decoration function. Called when a user 
	 * requests a dedicated connection to the JMS broker by specifying a client ID.
	 * Gives an opportunity to mangle and/or decorate the client ID specified to ensure ID
	 * uniqueness per user (see "Use of shared and/or durable subscriptions and scalability 
	 * constraints" in the docs). Please note that the connection ID may change 
	 * unexpectedly and should not be used as part of the name mangling/decoration. Note also
	 * that this method may be called more than once for the same broker connection, 
	 * in this case the mangled/decorated name should always be the same. Default
	 * implementation simply returns the specified client ID.
	 * 
	 * @param connectionId the unique identifier of the user's connection.
	 * @param jmsConnector the name of the JMS Connector the user is trying
	 * to open a dedicated JMS broker connection on.
	 * @param clientId the client ID specified by the user for the JMS broker connection.
	 * 
	 * @return the clientID that must be used for the JMS broker connection.
	 */
	public String getDedicatedBrokerConnectionName(String connectionId, String jmsConnector, String clientId) {
		return clientId;
	}
	
	/**
	 * Durable subscription name mangling or decoration function. Called when a user requests 
	 * a durable subscription to the JMS broker. Gives an opportunity to mangle and/or decorated 
	 * the specified subscription name to ensure name uniqueness per user (see "Use of 
	 * durable subscriptions and scalability constraints" in the docs). Please note
	 * that the connection ID may change unexpectedly and should not be used as
	 * part of the name mangling/decoration. Note also that this method may be called more 
	 * than once for the same subscription, in this case the mangled/decorated name should 
	 * always be the same. Default implementation simply returns the specified 
	 * subscription name.
	 * 
	 * @param connectionId the unique identifier of the user's connection.
	 * @param jmsConnector the name of the JMS Connector the user is trying
	 * to open the durable subscription on.
	 * @param clientId the client ID specified by the user for the JMS connection.
     * @param sessionGuid the GUID of the user's JMS session.
	 * @param subscriptionName the name of the durable subscription specified
	 * by the client.
	 * @param topicName the name of the topic addressed by the durable subscription.
	 * 
	 * @return the subscription name that must be used for the durable subscription.
	 */
	public String getDurableSubscriptionName(String connectionId, String jmsConnector, String clientId, String sessionGuid, String subscriptionName, String topicName) {
		return subscriptionName;
	}
	
    /**
     * Shared subscription name mangling or decoration function. Called when a user requests 
     * a shared subscription to the JMS broker. Gives an opportunity to mangle and/or decorated 
     * the specified subscription name to ensure name uniqueness per user (see "Use of 
     * shared subscriptions and scalability constraints" in the docs). Please note
     * that the connection ID may change unexpectedly and should not be used as
     * part of the name mangling/decoration. Note also that this method may be called more 
     * than once for the same subscription, in this case the mangled/decorated name should 
     * always be the same. Default implementation simply returns the specified 
     * subscription name.
     * 
     * @param connectionId the unique identifier of the user's connection.
     * @param jmsConnector the name of the JMS Connector the user is trying
     * to open the shared subscription on.
     * @param clientId the client ID specified by the user for the JMS connection.
     * @param sessionGuid the GUID of the user's JMS session.
     * @param subscriptionName the name of the shared subscription specified
     * by the client.
     * @param topicName the name of the topic addressed by the shared subscription.
     * 
     * @return the subscription name that must be used for the shared subscription.
     */
    public String getSharedSubscriptionName(String connectionId, String jmsConnector, String clientId, String sessionGuid, String subscriptionName, String topicName) {
        return subscriptionName;
    }
    
    /**
     * Shared durable subscription name mangling or decoration function. Called when a user requests 
     * a shared durable subscription to the JMS broker. Gives an opportunity to mangle and/or decorated 
     * the specified subscription name to ensure name uniqueness per user (see "Use of 
     * shared durable subscriptions and scalability constraints" in the docs). Please note
     * that the connection ID may change unexpectedly and should not be used as
     * part of the name mangling/decoration. Note also that this method may be called more 
     * than once for the same subscription, in this case the mangled/decorated name should 
     * always be the same. Default implementation simply returns the specified 
     * subscription name.
     * 
     * @param connectionId the unique identifier of the user's connection.
     * @param jmsConnector the name of the JMS Connector the user is trying
     * to open the shared durable subscription on.
     * @param clientId the client ID specified by the user for the JMS connection.
     * @param sessionGuid the GUID of the user's JMS session.
     * @param subscriptionName the name of the shared durable subscription specified
     * by the client.
     * @param topicName the name of the topic addressed by the shared durable subscription.
     * 
     * @return the subscription name that must be used for the shared durable subscription.
     */
    public String getSharedDurableSubscriptionName(String connectionId, String jmsConnector, String clientId, String sessionGuid, String subscriptionName, String topicName) {
        return subscriptionName;
    }
}
