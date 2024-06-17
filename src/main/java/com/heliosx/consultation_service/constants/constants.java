package com.heliosx.consultation_service.constants;

public class constants {
    /*Genvovian Pear condition*/
    public final static Long GENOVIAN_PEAR_CONDITION_ID = 1L;
    public final static Long REACTION_TO_GENOVIAN_PEAR_QUESTION_ID = 1L;
    public static final String REACTION_TO_GENOVIAN_PEAR_QUESTION_TEXT = "Have you had a bad reaction to Genovian Pear?";
    public final static String PRESCRIPTION_BLOCKER_MSG_NO_SYMPTOMS = "You cannot get a prescription if you are not showing symptoms resulting from exposure to Genovian Pear.";
    public final static Long MEDICATION_PREVIOUSLY_PRESCRIBED_QUESTION_ID = 2L;
    public static final String MEDICATION_PREVIOUSLY_PRESCRIBED_QUESTION_TEXT = "Have you taken medication for this condition before?";
    public final static String PRESCRIPTION_BLOCKER_MSG_MEDICATION_PREVIOUSLY_PRESCRIBED = "You cannot proceed if you have taken a prescription for Genovian Pear before.";
    public final static String UNKNOWN_CONDITION_MSG = "Could not find consultation questions for the given condition";
    public final static String UNABLE_TO_PROCESS_QUESTIONS_MSG = "An error occurred processing the consultation questions";
    public final static String MISSING_CONSULTATION_QUESTION_MSG = "Did not receive all required consultation questions";
}
