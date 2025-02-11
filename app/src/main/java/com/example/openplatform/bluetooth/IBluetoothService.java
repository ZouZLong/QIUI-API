package com.example.openplatform.bluetooth;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IBluetoothService extends IInterface {
    void callBluetoothApi(int var1, Bundle var2, IResponse var3) throws RemoteException;

    abstract class Stub extends Binder implements IBluetoothService {
        private static final String DESCRIPTOR = "com.inuker.bluetooth.library.IBluetoothService";
        static final int TRANSACTION_callBluetoothApi = 1;

        public Stub() {
            this.attachInterface(this, "com.inuker.bluetooth.library.IBluetoothService");
        }

        public static IBluetoothService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("com.inuker.bluetooth.library.IBluetoothService");
                return iin != null && iin instanceof IBluetoothService ? (IBluetoothService)iin : new Proxy(obj);
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface("com.inuker.bluetooth.library.IBluetoothService");
                    int _arg0 = data.readInt();
                    Bundle _arg1;
                    if (0 != data.readInt()) {
                        _arg1 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }

                    IResponse _arg2 =IResponse.Stub.asInterface(data.readStrongBinder());
                    this.callBluetoothApi(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    if (_arg1 != null) {
                        reply.writeInt(1);
                        _arg1.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 1598968902:
                    reply.writeString("com.inuker.bluetooth.library.IBluetoothService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IBluetoothService {
            private final IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "com.inuker.bluetooth.library.IBluetoothService";
            }

            public void callBluetoothApi(int code, Bundle args, IResponse response) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("com.inuker.bluetooth.library.IBluetoothService");
                    _data.writeInt(code);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeStrongBinder(response != null ? response.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (0 != _reply.readInt()) {
                        args.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }
}
