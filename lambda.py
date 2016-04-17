from __future__ import print_function
import json
import urllib2


APP_ID = "your-app-id-as-string" #App-id taken from alexa.amazon.com
BACKEND_URL = "your-listener-address/api/echo"

NO_PARAMETERS = {}
NO_REPROMPT = None
DO_NOT_END_SESSION = False

def lambda_handler(event, context):
    print("event.session.application.applicationId=" +
          event['session']['application']['applicationId'])

    if event['session']['application']['applicationId'] != APP_ID:
        raise ValueError("Invalid Application ID")

    if event['session']['new']:
        print("### NEW SESSION ###")
        on_session_started({'requestId': event['request']['requestId']},
                           event['session'])

    if event['request']['type'] == "LaunchRequest":
        return on_launch(event['request'], event['session'])
    elif event['request']['type'] == "IntentRequest":
        return on_intent(event['request'], event['session'])
    elif event['request']['type'] == "SessionEndedRequest":
        return on_session_ended(event['request'], event['session'])


def on_session_started(session_started_request, session):
    print("on_session_started requestId=" + session_started_request['requestId']
          + ", sessionId=" + session['sessionId'])


def on_launch(launch_request, session):
    print("on_launch requestId=" + launch_request['requestId'] +
          ", sessionId=" + session['sessionId'])
    return get_welcome_response()


def on_intent(intent_request, session):
    """Processes available intents which are defined in application config.
       This is the only place in lambda which should be modified when user wants to add more intents
       and send it to a listener in generic way."""
    AVAILABLE_INTENTS = [name + 'Intent' for name in ['CheckAnswer',
                                                      'Assessment',
                                                      'PrepareCard',
                                                      'ConfirmCard',
                                                      'GoToCards',
                                                      'GoToLearning',
                                                      'HowManyWords']]

    print("on_intent requestId=" + intent_request['requestId'] +
          ", sessionId=" + session['sessionId'])

    intent = intent_request['intent']
    intent_name = intent_request['intent']['name']

    if intent_name == "AMAZON.HelpIntent":
        return get_help_response()
    elif intent_name == "AMAZON.CancelIntent" or intent_name == "AMAZON.StopIntent":
        return handle_session_end_request()
    elif intent_name in AVAILABLE_INTENTS:
        return get_answer_from_server(intent, session)
    else:
        raise ValueError("Invalid intent - check configuration")


def on_session_ended(session_ended_request, session):
    print("on_session_ended requestId=" + session_ended_request['requestId'] +
          ", sessionId=" + session['sessionId'])


def send_request_to_server(name, parameters):
    """Parameters are sent to a listener in json format"""
    payload = json.dumps({"name": name,
                          "parameters": parameters})
    print("# Payload:")
    print(payload)
    
    try:
        # can't use requests lib
        req = urllib2.Request(BACKEND_URL, payload, {'Content-Type': 'application/json'})
        f = urllib2.urlopen(req, timeout=15)
        response = f.read()
        f.close()
        return response
    except urllib2.URLError:
        print("## URLError EXCEPTION ##")
        return "Couldn't find server address... Are you sure it is correct in lambda?"
    except Exception:
        print("## Generic EXCEPTION ##")
        return "Unhandled exception while trying to get response from a server."



def get_answer_from_server(intent, session):
    """Sends parameters from intent to process it on a server. After that, a response for Echo is built"""
    print("### GENERIC RESPONDER ###")
    session_attributes = {}
    parameters = {}
    print("## Intent:")
    print(intent)
    card_title = intent['name'].replace('Intent', '')
    speech_output = ''
    try:
        if 'slots' in intent:
            for (k, v) in intent['slots'].items():
                parameters[v['name'].lower()] = v['value']
        speech_output = send_request_to_server(intent['name'], parameters)
    except ValueError:
        # Happens when beginning is OK but value was not recognized - then VALUE is not present in dictionary
        print("## ValueError EXCEPTION ##")
        speech_output = "I didn't understand the value of this intent. Please repeat your request."
    return build_response(session_attributes, build_speechlet_response(
        card_title, speech_output, NO_REPROMPT, DO_NOT_END_SESSION))


def get_help_response():
    print("### HELP ###")
    session_attributes = {}
    card_title = "Help"
    speech_output = "I can let you create new cards with things you want to learn," \
                    "tell you if the answer to a question is correct," \
                    "let you evaluate how well you know prepared cards," \
                    "switch between views and check how many cards you want to repeat for a given day"

    return build_response(session_attributes, build_speechlet_response(
        card_title, speech_output, NO_REPROMPT, DO_NOT_END_SESSION))


def get_welcome_response():
    session_attributes = {}
    card_title = "Welcome"
    speech_output = "Welcome to learning app"

    # If the user either does not reply to the welcome message or says something
    # that is not understood, they will be prompted again with this text.
    reprompt_text = "Still waiting..."

    return build_response(session_attributes, build_speechlet_response(
        card_title, speech_output, reprompt_text, DO_NOT_END_SESSION))


def handle_session_end_request():
    card_title = "Session Ended"
    speech_output = "Thank you!" \
                    "Have a nice day! "
    # Setting this to true ends the session and exits the skill.
    should_end_session = True
    return build_response({}, build_speechlet_response(
        card_title, speech_output, None, should_end_session))


# --------------- Helpers that build all of the responses ----------------------
def build_speechlet_response(title, output, reprompt_text, should_end_session):
    return {
        'outputSpeech': {
            'type': 'PlainText',
            'text': output
        },
        'card': {
            'type': 'Simple',
            'title': 'SessionSpeechlet - ' + title,
            'content': 'SessionSpeechlet - ' + output
        },
        'reprompt': {
            'outputSpeech': {
                'type': 'PlainText',
                'text': reprompt_text
            }
        },
        'shouldEndSession': should_end_session
    }


def build_response(session_attributes, speechlet_response):
    return {
        'version': '1.0',
        'sessionAttributes': session_attributes,
        'response': speechlet_response
    }
