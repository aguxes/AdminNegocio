package Clases;

public class Fecha {

    protected int dia;
    protected int mes;
    protected int anio;

    public Fecha(int dia, int mes, int anio) {
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
    }

    public int getDia() {return dia;}

    public void setDia(int dia) {this.dia = dia;}

    public int getMes() {return mes;}

    public void setMes(int mes) {this.mes = mes;}

    public int getAnio() {return anio;}

    public void setAnio(int anio) {this.anio = anio;}
/*
    void Fecha::cargarFecha() {
        Menu menu;
        bool validacion = false;
        while (!validacion) {
            int input;
            Menu::setColor(7);
            cout << "Ingrese el dia: ";
            Menu::setColor(0);
            cin >> input;
            dia = input;
            Menu::setColor(7);
            cout << "Ingrese el mes: ";
            Menu::setColor(0);
            cin >> input;
            mes = input;
            Menu::setColor(7);
            cout << "Ingrese el anio: ";
            Menu::setColor(0);
            cin >> input;
            anio = input;
            validacion = validar();
            if (validacion) {
                Menu::setColor(2);
                cout << "La fecha se guardo correctamente" << endl;
            } else {

                menu.mensajeDeError("Error, Ingrese una fecha real");
            }
            Menu::setColor(7);
        }
    }

    bool Fecha::validar() {
        if (anio > 1800 && anio < 2025) {
            if (mes < 1 || mes > 12) {
                return false;
            }
            switch (mes) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    return (dia >= 1 && dia <= 31);
                case 4:
                case 6:
                case 9:
                case 11:
                    return (dia >= 1 && dia <= 30);
                case 2:
                    return (dia <= 29);
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    void Fecha::RestarDia() {
        dia--;
        if (!validar()) {
            mes--;
            if (mes < 1) {
                mes = 12;
                anio--;
            }
            if (mes != 2) {
                int diasPorMes[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
                dia = diasPorMes[mes - 1];
            }
        }
    }

    void Fecha::mostrarFecha() {
        Menu::setColor(0);
        cout << dia << "/" << mes << "/" << anio << endl;
        Menu::setColor(7);
    }

    char* Fecha::toString() {
        char* buffer = new char[11];
        snprintf(buffer, 11, "%02d/%02d/%04d", dia, mes, anio);
        return buffer;

     */
}
