import React, { createContext, useEffect, useState } from "react";

interface ActionBtnProps {
  onClick: () => void;
  label: string;
  classNames?: string;
}

export interface IModalContext {
  show: (show?: boolean) => void;
  setupModal: (
    title: string | null,
    massage: string,
    canClose: boolean,
    actions: ActionBtnProps[]
  ) => void;
  reset: () => void;
}

const defaultModalContext: IModalContext = {
  show: () => {},
  setupModal: () => {},
  reset: () => {},
};

interface ModalState {
  message: string;
  title: string | null;
  canClose?: boolean;
  actions?: ActionBtnProps[];
}

const DEFAULT_MODAL_STATE: ModalState = {
  title: null,
  message: "",
  canClose: true,
};

export const ModalContext = createContext<IModalContext>(defaultModalContext);

export const ModalContextProvider = ({ children }: React.PropsWithChildren) => {
  const [showModal, setShowModal] = useState(false);
  const [modalProperties, setModalProperites] = useState(DEFAULT_MODAL_STATE);

  const handleShowModal = (show: boolean = true) => {
    setShowModal(show);
  };

  const handleSetupModal = (
    title: string | null,
    massage: string,
    canClose: boolean,
    actions: ActionBtnProps[]
  ) => {
    setModalProperites({
      ...DEFAULT_MODAL_STATE,
      title: title,
      actions: actions,
      canClose: canClose,
      message: massage,
    });
  };

  const handleReset = () => {
    setModalProperites(DEFAULT_MODAL_STATE);
    setShowModal(false);
  };

  return (
    <ModalContext.Provider
      value={{
        show: handleShowModal,
        reset: handleReset,
        setupModal: handleSetupModal,
      }}
    >
      <input
        type="checkbox"
        className="modal-toggle hidden"
        checked={showModal}
        onChange={(e) => setShowModal(e.target.checked)}
      />
      <div className="modal">
        <div className="modal-box relative">
          {modalProperties.canClose && (
            <label
              className="btn btn-sm btn-circle absolute right-2 top-2"
              onClick={() => setShowModal(false)}
            >
              ✕
            </label>
          )}
          {modalProperties.title && (
            <h3 className="text-lg font-bold">{modalProperties.title}</h3>
          )}
          <p className="py-4">{modalProperties.message}</p>
          <div className="modal-action">
            {modalProperties.actions?.map(({ onClick, label, classNames }) => (
              <button
                className={`btn ${classNames ?? ""}`}
                onClick={() => {
                  onClick();
                  setShowModal(false);
                }}
                key={label}
              >
                {label}
              </button>
            ))}
          </div>
        </div>
      </div>
      {children}
    </ModalContext.Provider>
  );
};
