import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog{
    private JTextField emailTF;
    private JPasswordField passwordTF;
    private JButton cancelButton;
    private JButton OKButton;
    private JPanel loginPanel;
    public User user;
    public LoginForm (JFrame parent){
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(640,480));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);
        //setVisible(true);

        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email=emailTF.getText();
                String password=String.valueOf(passwordTF.getPassword());
                System.out.println("boton ok");
                user=getAuthenticationUser(email,password);

                if (user!=null){
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog(
                            LoginForm.this,"email o password incorrectos",
                            "intente nuevamente",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            }
        });


        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("boton cancel");
                dispose();
            }
        });
        setVisible(true);
    }


    private User getAuthenticationUser(String email, String password){
        User user =null;

        final String DB_URL="jdbc:mysql://localhost/farmacia?serverTimezone=UTC";
        final String USERNAME="cualquiera";
        final String PASSWORD="";


        try{
            Connection conn= DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            Statement stmt= conn.createStatement();
            String sql="SELECT * FROM usuario WHERE EMAIL=? AND CONTRASEÑA=?";
            PreparedStatement preparedStatement=conn.prepareStatement(sql);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            System.out.println("conexion ok");
            ResultSet resultSet=preparedStatement.executeQuery();

            if(resultSet.next()){
                user=new User();
                user.NOMBRE=resultSet.getString("NOMBRE");
                user.EMAIL=resultSet.getString("EMAIL");
                user.CELULAR=resultSet.getString("CELULAR");
                user.DIRECCION=resultSet.getString("DIRECCION");
                user.CONTRASEÑA=resultSet.getString("CONTRASEÑA");
            }

            stmt.close();
            conn.close();

        }catch(Exception e){
            System.out.println("error de...");
            e.printStackTrace();
        }

        return user;
    }


    public static void main(String[] args) {
        LoginForm loginForm=new LoginForm(null);
        User user =loginForm.user;

        if(user!=null){
            System.out.println("Autenticacion correcta:"+user.NOMBRE);
            System.out.println("email: "+user.EMAIL);
            //System.out.println("celular: "+user.celular);
            System.out.println("direccion: "+user.DIRECCION);
            System.out.println("clave: "+user.CONTRASEÑA);
        }
        else{
            System.out.println("Autenticacion fallida");
        }
    }
}

