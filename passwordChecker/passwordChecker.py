#!/usr/bin/env python

import crypt
import getpass
import pwd
import spwd
import os

def has_current_user_root_privileges():
    return os.geteuid() == 0

def unix_check_credentials(username, password):
    cryptedpasswd = None
    try:
        cryptedpasswd = unix_crypted_passwd(username)
    except KeyError:
        print('user %s doesn\'t exist!' % username)
        exit(-1)
    except NotImplementedError:
        print('Doesn\'t work with standard password check,'
                + 'try now shadow password check')
        
    if not cryptedpasswd:
        try:
            cryptedpasswd = unix_crypted_shadow_passwd(username)
        except KeyError:
            print('user %s doesn\'t exist!' % username)
        except NotImplementedError:
            print('Can\'t get shadow password!')
            print(ex)

    if cryptedpasswd:
        return crypt.crypt(password, cryptedpasswd) == cryptedpasswd
    else:
        print('Can\'t do the password check')

    return False

def unix_crypted_passwd(username):
    cryptedpasswd = pwd.getpwnam(username)[1]
    if cryptedpasswd and (cryptedpasswd == 'x' or cryptedpasswd == '*'):
        raise NotImplementedError(
                "Sorry, currently no support for shadow passwords")
    return cryptedpasswd

def unix_crypted_shadow_passwd(username):
    cryptedpasswd = spwd.getspnam(username)
    return cryptedpasswd[1]


def requestCredentials():
    username = raw_input('Username: ')
    password = getpass.getpass()

    return (username, password)

if __name__ == "__main__":
    username, password = requestCredentials()

    result = None
    if os.name == 'posix':
        if not has_current_user_root_privileges():
            print('you must have root rights to do the credential checks')
        else:
            result = unix_check_credentials(username, password)
    else:
        print('unsupported operating system')


    print("Successfull? %s" %  (result))

