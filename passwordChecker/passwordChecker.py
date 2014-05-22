#!/usr/bin/env python

import getpass
import os
import subprocess


def unix_load_modules():
    __import__('pwd')
    __import__('spwd')
    __import__('crypt')


def unix_has_current_user_root_privileges():
    return os.geteuid() == 0


def unix_check_credentials(username, password):
    crypted_password = None
    try:
        crypted_password = unix_crypted_password(username)
    except KeyError:
        print('user %s doesn\'t exist!' % username)
        exit(-1)
    except NotImplementedError:
        print('Doesn\'t work with standard password check,'
              + 'try now shadow password check')

    if not crypted_password:
        try:
            crypted_password = unix_crypted_shadow_password(username)
        except KeyError:
            print('user %s doesn\'t exist!' % username)
        except NotImplementedError:
            print('Can\'t get shadow password!')
            print(ex)

    if crypted_password:
        return crypt.crypt(password, crypted_password) == crypted_password
    else:
        print('Can\'t do the password check')

    return False


def unix_crypted_password(username):
    crypted_password = pwd.getpwnam(username)[1]
    if crypted_password and (crypted_password == 'x' or crypted_password == '*'):
        raise NotImplementedError("Sorry, currently no support for shadow passwords")
    return crypted_password


def unix_crypted_shadow_password(username):
    crypted_password = spwd.getspnam(username)[1]
    return crypted_password


def nt_check_credentials(username, password):
    s = """
$Username = '%s'
$Password = '%s'
$pass = ConvertTo-SecureString -AsPlainText $Password -Force
$Cred = New-Object System.Management.Automation.PSCredential -ArgumentList $Username,$pass
Start-Process ping localhost -NoNewWindow -Wait -Credential $Cred
        """ % (username, password)

    exit_code = subprocess.call(["powershell", s], stdin=(open(os.devnull, 'r')), stdout=(open(os.devnull, 'w')),
                                stderr=subprocess.STDOUT)
    return exit_code


def request_credentials_from_console():
    username = raw_input('Username: ')
    password = raw_input('Password: ')
    return username, password


if __name__ == "__main__":
    username, password = request_credentials_from_console()
    print(username, password)

    result = None
    if os.name == 'posix':
        unix_load_modules()
        if not unix_has_current_user_root_privileges():
            print('you must have root rights to do the credential checks')
        else:
            result = unix_check_credentials(username, password)
    elif os.name == 'nt':
        result = nt_check_credentials(username, password)
    else:
        print('unsupported operating system')

    print("Successful? %s" % result)

