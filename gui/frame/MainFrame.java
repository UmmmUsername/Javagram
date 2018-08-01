package gui.frame;

import components.MyLayeredPane;
import components.OverlayDialog;
import gui.Helper;
import gui.contacts.ContactsList;
import gui.intro.CodeForm;
import gui.intro.PhoneForm;
import gui.intro.Registration;
import gui.main.MainForm;
import gui.messages.MessagesForm;
import gui.overlays.*;
import org.javagram.dao.*;
import org.javagram.dao.Dialog;
import org.javagram.dao.proxy.TelegramProxy;
import org.javagram.dao.proxy.changes.UpdateChanges;
import undecorated.ComponentResizerAbstract;
import undecorated.Undecorated;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MainFrame extends JFrame {
    private Undecorated undecoratedFrame;

    private TelegramDAO telegramDAO;
    private TelegramProxy telegramProxy;

    private PhoneForm phoneForm = new PhoneForm();
    private Registration registrationForm = new Registration();
    private CodeForm codeForm = new CodeForm();
    private MainForm mainForm = new MainForm();
    private ContactsList contactsList = new ContactsList();

    private ProfileForm profileForm = new ProfileForm();
    private AddContactForm addContactForm = new AddContactForm();

    private OverlayDialog overlayDialog = new OverlayDialog();
    private EditContactForm editContactForm = new EditContactForm();

    private MyLayeredPane contactsLayeredPane = new MyLayeredPane();
    private PlusOverlay plusOverlay = new PlusOverlay();

    private int messagesFrozen;

    {
        undecoratedFrame = new Undecorated(this, ComponentResizerAbstract.KEEP_RATIO_CENTER);
        undecoratedFrame.setContentPanel(overlayDialog);
//        undecoratedFrame.setContentPanel(editContactForm);

        showPhoneNumberRequest(false);
        setSize(925 + 4, 390 + 39);
        setMinimumSize(new Dimension(905, 596 + 35));// + 18
        setLocationRelativeTo(null);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                exit();
            }
        });

        phoneForm.addActionListenerForConfirm(e -> switchFromPhone());

        codeForm.addActionListenerForConfirm(actionEvent -> switchFromCode());

        registrationForm.addActionListenerForConfirm(actionEvent -> switchFromRegistration());

//        mainForm.setContactsPanel(contactsList);

        mainForm.setContactsPanel(contactsLayeredPane);
        contactsLayeredPane.add(contactsList, new Integer(0));
        contactsLayeredPane.add(plusOverlay, new Integer(1));

        mainForm.addSearchEventListener(actionEvent -> searchFor(mainForm.getSearchText()));

        plusOverlay.addActionListener(e -> {
            ContactInfo contactInfo = new ContactInfo();
            Person person = contactsList.getSelectedValue();
            if (person instanceof KnownPerson && !(person instanceof Contact))
                contactInfo.setPhone(((KnownPerson) person).getPhoneNumber());
            addContactForm.setContactInfo(contactInfo);
            changeOverlayPanel(addContactForm);
        });

        addContactForm.addBackButtonListener(e -> changeOverlayPanel(null));

        addContactForm.addApplyButtonListener(e -> tryAddContact(addContactForm.getContactInfo()));

        contactsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (listSelectionEvent.getValueIsAdjusting() || messagesFrozen != 0)
                    return;

                if (telegramProxy == null) {
                    displayDialog(null);
                } else {
                    displayDialog(contactsList.getSelectedValue());
                }
            }
        });

        mainForm.addSettingsButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ContactInfo contactInfo =
                        Helper.toContactInfo(telegramProxy.getMe(), telegramProxy, false, false);
                profileForm.setInfo(contactInfo);

                changeOverlayPanel(profileForm);
            }
        });

        mainForm.addSendButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Person buddy = contactsList.getSelectedValue();
                String text = mainForm.getMessageText().trim();
                if (telegramProxy != null && buddy != null && !text.isEmpty()) {
                    try {
                        telegramProxy.sendMessage(buddy, text);
                        mainForm.setMessageText("");
                        checkForUpdates(true);
                    } catch (Exception e) {
                        showWarningMessage("Не могу отправить сообщение");
                    }
                }
            }
        });

        mainForm.addEditButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Person person = contactsList.getSelectedValue();
                if (person instanceof Contact) {
                    editContactForm.setContactInfo(Helper.toContactInfo((Contact) person, telegramProxy, false, true));
                    changeOverlayPanel(editContactForm);
                }
            }
        });

        profileForm.addBackButtonListener(e -> changeOverlayPanel(null));
        profileForm.addExitButtonListener(e -> switchToBegin());

        editContactForm.addActionListenerForReturn(actionEvent -> changeOverlayPanel(null));
        editContactForm.addActionListenerForSave(actionEvent -> tryUpdateContact(editContactForm.getContactInfo()));
        editContactForm.addActionListenerForDelete(actionEvent -> tryDeleteContact(editContactForm.getContactInfo()));
    }

    public MainFrame(TelegramDAO telegramDAO) throws HeadlessException {
        this.telegramDAO = telegramDAO;
    }

    private void switchFromPhone() {
        String phoneNumber = phoneForm.getPhoneNumber();
        if (phoneNumber == null) {
            showPhoneNumberEmpty();
        } else {
            switchFromPhone(phoneNumber);
        }
    }

    private void switchFromPhone(String phoneNumber) {
        try {
            try {
                telegramDAO.acceptNumber(phoneNumber.replaceAll("[\\D]+", ""));

                if (telegramDAO.canSignUp()) {
                    if (!showQuestionMessage("Пользователь не зарегистрирован. Будет регистрироваться?")) {
                        showPhoneNumberRequest(true);
                        return;
                    }
                }
                codeForm.setPhoneLabelText(phoneNumber);
                sendAndRequestCode();
            } catch (ApiException e) {
                if (e.isPhoneNumberInvalid()) {
                    showPhoneNumberInvalid();
                    return;
                }
                throw e;
            }
        } catch (Exception e) {
            catchException(e);
        }
    }

    private void switchFromCode() {
        String code = codeForm.getCode();
        if (code == null || code.isEmpty()) {
            showCodeEmpty();
        } else {
            switchFromCode(code);
        }
    }

    private void switchFromCode(String code) {
        try {
            try {
                telegramDAO.signIn(code);
                switchToMainScreen(true);
            } catch (ApiException e) {
                if (e.isCodeInvalid()) {
                    showCodeInvalid();
                    return;
                }
                if (e.isCodeEmpty()) {
                    showCodeEmpty();
                    return;
                }
                if (e.isCodeExpired()) {
                    showCodeExpired();
                    return;
                }
                if (e.isPhoneNumberUnoccupied()) {
                    showNameRequest(true);
                    return;
                }
                throw e;
            }
        } catch (Exception e) {
            catchException(e);
        }
    }

    private void switchFromRegistration() {
        String firstName = registrationForm.getFirstName();
        String lastName = registrationForm.getLastNameText();
        if ((firstName == null || firstName.isEmpty())
                && (lastName == null || lastName.isEmpty())) {
            showNameInvalid();
        } else {
            switchFromRegistration(firstName, lastName, codeForm.getCode());
        }
    }

    private void switchFromRegistration(String firstName, String lastName, String code) {
        try {
            try {
                telegramDAO.signUp(code, firstName, lastName);
                switchToMainScreen(true);
            } catch (ApiException e) {
                if (e.isNameInvalid()) {
                    showNameInvalid();
                    return;
                }
                if (e.isCodeExpired()) {
                    showCodeExpired();
                    return;
                }
                throw e;
            }
        } catch (Exception e) {
            catchException(e);
        }
    }

    private void showPhoneNumberRequest(boolean clear) {
        changeContentPanel(phoneForm);
        if (clear)
            phoneForm.clear();
        phoneForm.transferFocusTo();
    }

    private void showPhoneNumberEmpty() {
        showWarningMessage("Пожалуйста, введите корректный номер телефона.");
        showPhoneNumberRequest(true);
    }

    private void showCodeRequest() {
        changeContentPanel(codeForm);
        codeForm.clear();
        codeForm.transferFocusTo();
    }

    private void sendAndRequestCode() throws IOException, ApiException {
        sendCode();
        showCodeRequest();
    }

    private void sendCode() throws IOException, ApiException {
        telegramDAO.sendCode();
    }

    private void showPhoneNumberInvalid() {
        showWarningMessage("Номер телефона введен не верно");
        showPhoneNumberRequest(true);
    }

    private void showCodeEmpty() {
        showWarningMessage("Не введен код");
        showCodeRequest();
    }

    private void showCodeInvalid() {
        showWarningMessage("Неверный код");
        showCodeRequest();
    }

    private void showCodeExpired() throws IOException, ApiException {
        showWarningMessage("Код устарел. Отправляю новый");
        sendAndRequestCode();
    }

    private void showNameRequest(boolean clear) {
        changeContentPanel(registrationForm);
        if (clear)
            registrationForm.clear();
    }

    private void showNameInvalid() {
        showWarningMessage("Неверные регистрационные данные");
        showNameRequest(false);
    }

    private void updateTelegramProxy() {
        messagesFrozen++;
        try {
            contactsList.setTelegramProxy(telegramProxy);
            contactsList.setSelectedValue(null);
            createMessagesForm();
            displayDialog(null);
            displayMe(telegramProxy != null ? telegramProxy.getMe() : null);
        } finally {
            messagesFrozen--;
        }

        mainForm.revalidate();
        mainForm.repaint();
    }

    private void createTelegramProxy() throws ApiException {
        telegramProxy = new TelegramProxy(telegramDAO);
        updateTelegramProxy();
    }

    private void destroyTelegramProxy() {
        telegramProxy = null;
        updateTelegramProxy();
    }

    private void switchToMainScreen(boolean clear) throws ApiException {
        if (clear) {
            mainForm.clearFields();
        }

        changeContentPanel(mainForm);
        createTelegramProxy();
        contactsList.setTelegramProxy(telegramProxy);
        displayMe(telegramProxy == null ? null : telegramProxy.getMe());
    }

    private void displayMe(Me me) {
        if (me == null) {
            mainForm.setMeText(null);
            mainForm.setMePhoto(null);
        } else {
            mainForm.setMeText(me.getFirstName() + " " + me.getLastName());
            mainForm.setMePhoto(Helper.getPhoto(telegramProxy, me, true, true));
        }
    }

    private void displayBuddy(Person person) {
        if (person == null) {
            mainForm.setBuddyText(null);
            mainForm.setBuddyPhoto(null);
        } else {
            mainForm.setBuddyText(person.getFirstName() + " " + person.getLastName());
            mainForm.setBuddyPhoto(Helper.getPhoto(telegramProxy, person, true, true));
        }
    }

    private MessagesForm createMessagesForm() {
        MessagesForm messagesForm = new MessagesForm(telegramProxy);
        mainForm.setMessagesPanel(messagesForm);
        mainForm.revalidate();
        mainForm.repaint();
        return messagesForm;
    }

    private MessagesForm getMessagesForm() {
        if (mainForm.getMessagesPanel() instanceof MessagesForm) {
            return (MessagesForm) mainForm.getMessagesPanel();
        } else {
            return createMessagesForm();
        }
    }

    private void displayDialog(Person person) {
        try {
            MessagesForm messagesForm = getMessagesForm();
            messagesForm.display(person);
            displayBuddy(person);
            revalidate();
            repaint();
        } catch (Exception e) {
            showErrorMessage("Проблема соединения с сервером");
            abort(e);
        }
    }

    private void changeContentPanel(Container contentPanel) {
        overlayDialog.setContentPanel(contentPanel);
    }

    private void changeOverlayPanel(Container overlayPanel) {
        overlayDialog.setOverlayPanel(overlayPanel);
    }

    private void switchToBegin() {
        try {
            destroyTelegramProxy();
            changeOverlayPanel(null);
            showPhoneNumberRequest(true);
            telegramDAO.logOut();
        } catch (Exception e) {
            catchException(e);
        }
    }

    private void updateContacts() {
        messagesFrozen++;
        try {
            Person person = contactsList.getSelectedValue();
            contactsList.setTelegramProxy(telegramProxy);
            contactsList.setSelectedValue(person);
        } finally {
            messagesFrozen--;
        }
    }

    private void updateMessages() {
        displayDialog(contactsList.getSelectedValue());
        mainForm.revalidate();
        mainForm.repaint();
    }

    protected void checkForUpdates(boolean force) {
        if (telegramProxy != null) {
            UpdateChanges updateChanges = telegramProxy.update(force ? TelegramProxy.FORCE_SYNC_UPDATE : TelegramProxy.USE_SYNC_UPDATE);

            int photosChangedCount = updateChanges.getLargePhotosChanged().size() +
                    updateChanges.getSmallPhotosChanged().size() +
                    updateChanges.getStatusesChanged().size();

            if (updateChanges.getListChanged()) {
                updateContacts();
            } else if (photosChangedCount != 0) {
                contactsList.repaint();
            }

            Person currentBuddy = getMessagesForm().getPerson();
            Person targetPerson = contactsList.getSelectedValue();

            Dialog currentDialog = currentBuddy != null ? telegramProxy.getDialog(currentBuddy) : null;

            if (!Objects.equals(targetPerson, currentBuddy) ||
                    updateChanges.getDialogsToReset().contains(currentDialog) ||
                    updateChanges.getDialogsChanged().getDeleted().contains(currentDialog)) {
                updateMessages();
            } else if (updateChanges.getPersonsChanged().getChanged().containsKey(currentBuddy)
                    || updateChanges.getSmallPhotosChanged().contains(currentBuddy)
                    || updateChanges.getLargePhotosChanged().contains(currentBuddy)) {
                displayBuddy(targetPerson);
            }

            if (updateChanges.getPersonsChanged().getChanged().containsKey(telegramProxy.getMe())
                    || updateChanges.getSmallPhotosChanged().contains(telegramProxy.getMe())
                    || updateChanges.getLargePhotosChanged().contains(telegramProxy.getMe())) {
                displayMe(telegramProxy.getMe());
            }
        }
    }

    private void tryAddContact(ContactInfo info) {
        String phone = info.getClearedPhone();

        if (phone.isEmpty()) {
            showWarningMessage("Пожалуйста, введите номер телефона");
            return;
        }

        if (info.getFirstName().isEmpty() && info.getLastName().isEmpty()) {
            showWarningMessage("Пожалуйста, введите имя и/или фамилию");
            return;
        }

        for (Person person : telegramProxy.getPersons()) {
            if (person instanceof Contact) {
                if (((Contact) person).getPhoneNumber().replaceAll("\\D+", "").equals(phone)) {
                    showWarningMessage("Контакт с таким номером уже существует");
                    return;
                }
            }
        }

        try {
            telegramProxy.importContact(info.getPhone(), info.getFirstName(), info.getLastName());
        } catch (Exception e) {
            showWarningMessage("Ошибка на сервере при добавлении контакта");
        }

        changeOverlayPanel(null);
        checkForUpdates(true);
    }

    private void tryUpdateContact(ContactInfo info) {
        if (info.getFirstName().isEmpty() && info.getLastName().isEmpty()) {
            showWarningMessage("Пожалуйста, введите имя и/или фамилию");
            return;
        }

        try {
            telegramProxy.importContact(info.getPhone(), info.getFirstName(), info.getLastName());
        } catch (Exception e) {
            showWarningMessage("Ошибка на сервере при изменении контакта");
            return;
        }

        changeOverlayPanel(null);
        checkForUpdates(true);
    }

    private void tryDeleteContact(ContactInfo info) {
        int id = info.getId();

        try {
            telegramProxy.deleteContact(id);
        } catch (Exception e) {
            showWarningMessage("Ошибка на сервере при удалении контакта");
            return;
        }

        changeOverlayPanel(null);
        checkForUpdates(true);
    }

    private static boolean contains(String text, String... words) {
        for (String word : words) {
            if (text.contains(word))
                return true;
        }
        return false;
    }

    private static Person searchFor(String[] words, List<? extends Person> persons, Person current) {
        int currentIndex = persons.indexOf(current);

        for (int i = 1; i <= persons.size(); i++) {
            int index = (currentIndex + i) % persons.size();
            Person person = persons.get(index);

            if (contains(person.getFirstName().toLowerCase(), words)
                    || contains(person.getLastName().toLowerCase(), words)) {

                return person;
            }
        }

        return null;
    }

    private void searchFor(String text) {
        text = text.trim();

        if (text.isEmpty()) {
            return;
        }

        String[] words = text.toLowerCase().split("\\s+");
        List<Person> persons = telegramProxy.getPersons();
        Person person = contactsList.getSelectedValue();
        person = searchFor(words, persons, person);
        contactsList.setSelectedValue(person);

        if (person == null)
            showInformationMessage("Ничего не найдено");
    }

    private void abort(Throwable e) {
        if (e != null)
            e.printStackTrace();
        else
            System.err.println("Unknown Error");
        try {
            telegramDAO.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.exit(-1);
    }

    private void catchException(Exception e) {
        if (e instanceof IOException) {
            showErrorMessage("Потеряно соединение с сервером");
        } else if (e instanceof ApiException) {
            showErrorMessage("Непредвиденная ошибка API :: " + e.getMessage());
        } else {
            showErrorMessage("Непредвиденная ошибка");
        }
        abort(e);
    }

    private void exit() {
        try {
            telegramDAO.close();

            System.exit(0);
        } catch (Exception e) {
            abort(e);
        }
    }

    private void showErrorMessage(String text) {
        Undecorated.showDialog(this, text, "", JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, null,
                new Object[]{"Да", "Нет"}, "Да");
    }

    public void showWarningMessage(String text) {
        Undecorated.showDialog(this, text, "", JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION);
    }

    private void showInformationMessage(String text) {
        Undecorated.showDialog(this, text, "", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
    }

    public boolean showQuestionMessage(String text) {
        return Undecorated.showDialog(this, text, "", JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION, null,
                new Object[]{"Да", "Нет"}, "Да") == 0;
    }
}
