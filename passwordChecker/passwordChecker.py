#!/usr/bin/env python

import getpass
import os
import subprocess


def unix_load_modules():
    """
    Loads the modules which are only available on unix python environments.
    """
    __import__('pwd')
    __import__('spwd')
    __import__('crypt')


def unix_has_current_user_root_privileges():
    """
    Check if the current user has the root rights, just by verifying if the group uid is 0
    """
    return os.geteuid() == 0


def unix_check_credentials(username, password):
    """
    Checks if the passed credentials are valid on the local unix host.
    If they are valid the method returns true and false if not.

    The method raises a NotImplemtedError if the password can't be requested nor as standard crypted password, neither
    as crypted shadow password.
    """
    crypted_password = None

    # try to get the standard crypted password (may fail on system which not support it)
    try:
        crypted_password = unix_crypted_password(username)
    except KeyError:
        print('user %s doesn\'t exist!' % username)
        exit(-1)
    except NotImplementedError:
        print('Doesn\'t work with standard password check,'
              + 'try now shadow password check')

    # if the crypted_password isn't already successful requested, try the shadow password request.
    if not crypted_password:
        try:
            crypted_password = unix_crypted_shadow_password(username)
        except KeyError:
            print('user %s doesn\'t exist!' % username)
        except NotImplementedError:
            print('Can\'t get shadow password!')
            print(ex)

    # compare the crypted password with the reference password.
    if crypted_password:
        return crypt.crypt(password, crypted_password) == crypted_password
    else:
        print('Can\'t do the password check')

    return False


def unix_crypted_password(username):
    """
    Requests the crpyted password on unix systems for a specific user and returns it.

    If the unix system doesn't support the standard (obsolete?) password system, this method raises
    an NotImplementedError.
    """
    crypted_password = pwd.getpwnam(username)[1]
    if crypted_password and (crypted_password == 'x' or crypted_password == '*'):
        raise NotImplementedError("Sorry, currently no support for shadow passwords")
    return crypted_password


def unix_crypted_shadow_password(username):
    """
    Requests the crypted shadow password on unix systems for a specific user and returns it.
    """
    crypted_password = spwd.getspnam(username)[1]
    return crypted_password


def nt_check_credentials(username, password):
    """
    Checks the credentials passed as argument on windows NT systems.
    A simple ping command will be executed in a Powershell with the credentials passed.
    If the execution was successful, returns 0.
    """
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
    """
    Requests the credentials interactive and returns them in form (username, password)
    """
    username = raw_input('Username: ')
    password = raw_input('Password: ')
    return username, password


if __name__ == "__main__":
    username, password = request_credentials_from_console()
    print(username, password)

    result = None

    # Check current OS and select the appropriate method
    if os.name == 'posix':
        # Load the modules which can only be loaded in linux systems
        unix_load_modules()

        # The process has to be executed as root, if not no access to the password database can be done.
        if not unix_has_current_user_root_privileges():
            print('you must have root rights to do the credential checks')
        else:
            result = unix_check_credentials(username, password)

    elif os.name == 'nt':
        result = nt_check_credentials(username, password)

    else:
        print('unsupported operating system')

    print("Successful? %s" % result)

