package org.ezuce.example;

import org.ezuce.unitemedia.context.ServiceContext;
import org.ezuce.unitemedia.phone.Phone;

public class UnitePhone {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ServiceContext.start();
			Phone phone = new Phone();
			PhoneUI phoneUi = new PhoneUI();
			phoneUi.setPhone(phone);
			phoneUi.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}