LIB_PATH := ../unite-client/build/lib/dist
empty :=
space := $(empty) $(empty)
JARS = \
	$(LIB_PATH)/argdelegation-service.jar \
	$(LIB_PATH)/argdelegation.jar \
	$(LIB_PATH)/bouncycastle.jar \
	$(LIB_PATH)/certificate.jar \
	$(LIB_PATH)/credentialsstorage.jar \
	$(LIB_PATH)/dns.jar \
	$(LIB_PATH)/fmj.jar \
	$(LIB_PATH)/hid.jar \
	$(LIB_PATH)/httputil.jar \
	$(LIB_PATH)/ice4j.jar \
	$(LIB_PATH)/jain-sdp.jar \
	$(LIB_PATH)/jain-sip-api.jar \
	$(LIB_PATH)/jain-sip-ri.jar \
	$(LIB_PATH)/json-simple-1.1.1.jar \
	$(LIB_PATH)/jspeex.jar \
	$(LIB_PATH)/lcrypto-jdk16-143.jar \
	$(LIB_PATH)/libjitsi.jar \
	$(LIB_PATH)/log4j-1.2.8.jar \
	$(LIB_PATH)/netaddr-slick.jar \
	$(LIB_PATH)/netaddr.jar \
	$(LIB_PATH)/osgi.core.jar \
	$(LIB_PATH)/packetlogging.jar \
	$(LIB_PATH)/protocol-sip.jar \
	$(LIB_PATH)/protocol.jar \
	$(LIB_PATH)/smack.jar \
	$(LIB_PATH)/smackx.jar \
	$(LIB_PATH)/protocol-jabber.jar \
	$(LIB_PATH)/resourcemanager.jar \
	$(LIB_PATH)/sdes4j.jar \
	$(LIB_PATH)/swing-ui.jar \
	$(LIB_PATH)/sysactivitynotifications.jar \
	$(LIB_PATH)/util.jar \
	$(LIB_PATH)/version-impl.jar \
	$(LIB_PATH)/zrtp4j-light.jar \
	$(LIB_PATH)/xpp3.jar \
	$(LIB_PATH)/zrtp4j.jar \
	$(LIB_PATH)/protocol-sip-slick.jar \
	$(LIB_PATH)/httpcore-osgi-4.2.3.jar \
	$(LIB_PATH)/httpclient-osgi-4.2.3.jar \
	$(LIB_PATH)/hid-service.jar \
	$(LIB_PATH)/credentialsstorage-slick.jar

jar_SOURCE = $(shell find src -name '*.java')	
CLASSPATH = $(subst $(space),:,$(JARS)) 

build:
	test -d classes || mkdir classes
	javac -target 1.6 -source 1.6 -classpath $(CLASSPATH) -sourcepath src -d classes $(jar_SOURCE)
	jar -cf unitemedia.jar -C classes .
	mv unitemedia.jar $(LIB_PATH)

clean: 
	rm -rf classes